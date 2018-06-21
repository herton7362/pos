package com.kratos.module.auth.web;

import com.kratos.module.auth.domain.Admin;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class AdminSaveParam extends Admin {
    @ApiModelProperty("角色id")
    private List<String> roleIds;

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
