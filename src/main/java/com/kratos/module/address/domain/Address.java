package com.kratos.module.address.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@ApiModel("地址")
public class Address extends BaseEntity {
    @ApiModelProperty(value = "上级地址")
    @ManyToOne(fetch = FetchType.EAGER)
    private Address parent;
    @ApiModelProperty(value = "名称")
    @Column(length = 100)
    private String name;
    @ApiModelProperty(value = "级别")
    @Column(length = 50)
    private String level;

    public Address getParent() {
        return parent;
    }

    public void setParent(Address parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public enum Level {
        PROVINCE,
        CITY,
        AREA,
        COUNTY
    }
}
