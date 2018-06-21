package com.kratos.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.MappedSuperclass;

/**
 * 基础用户对象
 * @author tang he
 * @since 1.0.0
 */
@MappedSuperclass
public abstract class BaseUser extends BaseEntity {
    @ApiModelProperty(value = "登录名称")
    private String loginName;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "手机")
    private String mobile;
    @ApiModelProperty(value = "用户类型", notes="MEMBER,ADMIN")
    private String userType;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    protected void setUserType(String userType) {
        this.userType = userType;
    }

    public enum UserType {
        MEMBER,ADMIN
    }
}
