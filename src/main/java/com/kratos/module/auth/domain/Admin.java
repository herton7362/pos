package com.kratos.module.auth.domain;

import com.kratos.entity.BaseUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

/**
 * 管理员
 * @author tang he
 * @since 1.0.0
 */
@Entity
@ApiModel(value = "管理员")
public class Admin extends BaseUser {
    @ApiModelProperty(required = true, value = "姓名")
    @Column(length = 20)
    private String name;
    @ApiModelProperty(required = true, value = "角色")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="admin_roles",joinColumns={@JoinColumn(name="admin_id")},inverseJoinColumns={@JoinColumn(name="role_id")})
    private List<Role> roles;

    public Admin() {
        setUserType(UserType.ADMIN.name());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
