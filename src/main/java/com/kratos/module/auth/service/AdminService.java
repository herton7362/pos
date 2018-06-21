package com.kratos.module.auth.service;

import com.kratos.common.CrudService;
import com.kratos.module.auth.domain.Admin;
import com.kratos.module.auth.domain.Module;

import java.util.List;

public interface AdminService extends CrudService<Admin> {
    /**
     * 查询管理员菜单
     * @param id 管理员id
     * @return 菜单
     */
    List<Module> findModules(String id) throws Exception;
}
