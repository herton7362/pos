package com.framework.module.activity.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@ApiModel("联盟学院分类")
public class AllianceCollegeCategory extends BaseEntity {
    @ApiModelProperty(value = "上级分类")
    @ManyToOne(fetch = FetchType.EAGER)
    private AllianceCollegeCategory parent;
    @ApiModelProperty(value = "名称")
    @Column(length =100)
    private String name;

    public AllianceCollegeCategory getParent() {
        return parent;
    }

    public void setParent(AllianceCollegeCategory parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
