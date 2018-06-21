package com.kratos.module.auth.web;

import com.kratos.common.AbstractCrudController;
import com.kratos.module.auth.AdminThread;
import com.kratos.module.auth.domain.Admin;
import com.kratos.module.auth.domain.Module;
import com.kratos.module.auth.domain.Role;
import com.kratos.module.auth.service.AdminService;
import com.kratos.module.auth.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "管理员管理")
@RestController
@RequestMapping("/api/admin")
public class AdminController extends AbstractCrudController<Admin> {
    private final AdminService adminService;
    private final RoleService roleService;

    /**
     * 获取当前用户菜单
     */
    @ApiOperation(value="获取当前用户菜单")
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/menus", method = RequestMethod.GET)
    public ResponseEntity<List<Module>> menus() throws Exception {
        Admin admin = AdminThread.getInstance().get();
        return new ResponseEntity<>(adminService.findModules(admin.getId()), HttpStatus.OK);
    }

    /**
     * 根据id获取用户
     */
    @ApiOperation(value="根据id获取用户")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Admin> getOne(@PathVariable String id) throws Exception {
        Admin admin = adminService.findOne(id);
        AdminSaveParam adminSaveParam = new AdminSaveParam();
        BeanUtils.copyProperties(admin, adminSaveParam);
        List<Role> roles = admin.getRoles();
        List<String> ids = new ArrayList<>();
        if(roles != null) {
            roles.forEach(role -> ids.add(role.getId()));
        }
        adminSaveParam.setRoleIds(ids);
        return new ResponseEntity<>(adminSaveParam, HttpStatus.OK);
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(value = "/save/deprecated", method = RequestMethod.POST)
    public ResponseEntity<Admin> save(@RequestBody Admin t) throws Exception {
        t = adminService.save(t);
        return new ResponseEntity<>(t, HttpStatus.OK);
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Admin> save(@RequestBody AdminSaveParam t) throws Exception {
        Admin admin = new Admin();
        BeanUtils.copyProperties(t, admin);
        List<String> ids = t.getRoleIds();
        List<Role> roles = new ArrayList<>();
        ids.forEach(id -> {
            try {
                roles.add(roleService.findOne(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        admin.setRoles(roles);
        admin = adminService.save(admin);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @Autowired
    public AdminController(
            AdminService adminService,
            RoleService roleService
    ) {
        this.adminService = adminService;
        this.roleService = roleService;
    }

    @Override
    protected AdminService getService() {
        return adminService;
    }
}
