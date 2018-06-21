package com.kratos.module.auth.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("授权参数")
public class AuthorizeParam {
    @ApiModelProperty("角色id")
    private String roleId;
    @ApiModelProperty("授权的模块id")
    private List<String> moduleIds;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(List<String> moduleIds) {
        this.moduleIds = moduleIds;
    }
}
