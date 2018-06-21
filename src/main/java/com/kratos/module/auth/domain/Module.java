package com.kratos.module.auth.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * 模块
 * @author tang he
 * @since 1.0.0
 */
@Entity
@ApiModel("模块")
public class Module extends BaseEntity {
    @ApiModelProperty(value = "上级模块")
    @ManyToOne(fetch = FetchType.EAGER)
    private Module parent;
    @ApiModelProperty(value = "模块名称")
    @Column(length = 50)
    private String name;
    @ApiModelProperty(value = "功能代码")
    @Column(length = 100)
    private String code;
    @ApiModelProperty(value = "菜单请求路径")
    @Column(length = 500)
    private String url;
    @ApiModelProperty(value = "模块类型 MENU-菜单，FUNCTION-功能")
    @Column(length = 10)
    private String type;
    @ApiModelProperty(value = "模块名称路径存储树形结构路径")
    @Column(length = 500)
    private String naviNamePath;
    @ApiModelProperty(value = "模块id路径存储树形结构路径")
    @Column(length = 500)
    private String naviIdPath;
    @ApiModelProperty(value = "图标")
    @Column(length = 100)
    private String icon;

    public Module getParent() {
        return parent;
    }

    public void setParent(Module parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNaviNamePath() {
        return naviNamePath;
    }

    public void setNaviNamePath(String naviNamePath) {
        this.naviNamePath = naviNamePath;
    }

    public String getNaviIdPath() {
        return naviIdPath;
    }

    public void setNaviIdPath(String naviIdPath) {
        this.naviIdPath = naviIdPath;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public enum Type {
        MENU, FUNCTION
    }
}
