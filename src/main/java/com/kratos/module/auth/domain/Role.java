package com.kratos.module.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * 角色
 * @author tang he
 * @since 1.0.0
 */
@Entity
@ApiModel("角色")
public class Role extends BaseEntity {
    @ApiModelProperty(value = "角色名称")
    @Column(length = 50)
    private String name;
    @ManyToMany
    @JsonIgnore
    @JoinTable(name="role_modules",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="module_id")})
    @Where(clause="logically_deleted=0")
    private List<Module> modules;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}
