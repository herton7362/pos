package com.kratos.module.auth.service;

import com.kratos.common.AbstractCrudService;
import com.kratos.entity.BaseUser;
import com.kratos.module.auth.domain.Admin;
import com.kratos.module.auth.domain.AdminRepository;
import com.kratos.module.auth.domain.Module;
import com.kratos.module.auth.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class AdminServiceImpl extends AbstractCrudService<Admin> implements AdminService, UserService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin save(Admin admin) throws Exception {
        if(admin.getId() == null) {
            if(admin.getPassword() == null) {
                admin.setPassword("123456");
            }
            admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));
        } else {
            if(admin.getPassword() == null) {
                Admin temp = adminRepository.findOne(admin.getId());
                admin.setPassword(temp.getPassword());
            } else {
                admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));
            }
        }
        return super.save(admin);
    }

    @Override
    public BaseUser findOneByLoginName(String account) throws Exception {
        return adminRepository.findOneByLoginName(account);
    }

    @Override
    public BaseUser findOneByLoginNameAndClientId(String account, String clientId) throws Exception {
        return adminRepository.findOneByLoginNameAndClientId(account, clientId);
    }

    @Override
    public List<Module> findModules(String id) throws Exception {
        Admin admin = findOne(id);
        final List<Module> modulesNew = new ArrayList<>();
        List<Role> roles = admin.getRoles();
        roles.forEach(role -> modulesNew.addAll(role.getModules()));
        List<Module> result = modulesNew
                .stream()
                .filter(module -> Module.Type.MENU.name().equals(module.getType()))
                .sorted(Comparator.comparing(Module::getSortNumber))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    protected AdminRepository getRepository() {
        return adminRepository;
    }
}
