package com.kratos.module.auth.service;

import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.common.PageResult;
import com.kratos.common.utils.StringUtils;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class OauthClientDetailsServiceImpl extends AbstractCrudService<OauthClientDetails> implements OauthClientDetailsService {
    private final OauthClientDetailsRepository oauthClientDetailsRepository;
    private final AdminService adminService;
    private final ModuleService moduleService;
    private final RoleService roleService;

    @Override
    protected PageRepository<OauthClientDetails> getRepository() {
        return oauthClientDetailsRepository;
    }

    @Override
    public OauthClientDetails save(OauthClientDetails oauthClientDetails) throws Exception {
        if(StringUtils.isBlank(oauthClientDetails.getClientId())) {
            throw new BusinessException("client id is null");
        }
        if(StringUtils.isBlank(oauthClientDetails.getClientSecret())) {
            throw new BusinessException("client secret is null");
        }
        if(StringUtils.isBlank(oauthClientDetails.getId())) {
            initSuperAdmin(oauthClientDetails.getClientId());
        }
        return super.save(oauthClientDetails);
    }

    @Override
    public PageResult<OauthClientDetails> findAll(PageRequest pageRequest, Map<String, String[]> param) throws Exception {
        Page<OauthClientDetails> page = getRepository().findAll(getSpecificationForAllEntities(param), pageRequest);
        return new PageResult<>(page);
    }

    @Override
    public List<OauthClientDetails> findAll(Map<String, String[]> param) throws Exception {
        return getRepository().findAll(getSpecificationForAllEntities(param));
    }

    /**
     * 初始化超级管理员
     * @param clientId client id
     */
    private void initSuperAdmin(String clientId) throws Exception {
        Admin admin = new Admin();
        admin.setName("超级管理员");
        admin.setLoginName("admin");
        admin.setClientId(clientId);
        List<Role> roles = new ArrayList<>();
        roles.add(initRole(clientId));
        admin.setRoles(roles);
        adminService.save(admin);
    }

    /**
     * 初始化超级管理员角色
     * @param clientId client id
     */
    private Role initRole(String clientId)  throws Exception {
        Role role = new Role();
        role.setName("超级管理员");
        role.setClientId(clientId);
        role.setModules(initModule(clientId));
        return roleService.save(role);
    }

    private List<Module> initModule(String clientId) throws Exception {
        List<Module> modules = new ArrayList<>();
        Module authModule = createModule("权限管理", "", clientId, null);
        modules.add(authModule);
        modules.add(createModule("管理员管理", "/admin/admin/list", clientId, authModule));
        modules.add(createModule("角色管理", "/admin/role/list", clientId, authModule));
        modules.add(createModule("模块管理", "/admin/module/list", clientId, authModule));
        return modules;
    }

    private Module createModule(String name, String url, String clientId, Module parent)  throws Exception {
        Module module = new Module();
        module.setName(name);
        module.setType(Module.Type.MENU.name());
        module.setUrl(url);
        module.setClientId(clientId);
        module.setParent(parent);
        return moduleService.save(module);
    }

    @Override
    public OauthClientDetails findOneByClientId(String clientId) throws Exception {
        return oauthClientDetailsRepository.findOneByClientId(clientId);
    }

    @Autowired
    public OauthClientDetailsServiceImpl(
            OauthClientDetailsRepository oauthClientDetailsRepository,
            AdminService adminService,
            ModuleService moduleService,
            RoleService roleService
    ) {
        this.oauthClientDetailsRepository = oauthClientDetailsRepository;
        this.adminService = adminService;
        this.moduleService = moduleService;
        this.roleService = roleService;
    }
}
