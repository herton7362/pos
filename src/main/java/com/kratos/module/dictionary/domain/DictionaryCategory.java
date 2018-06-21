package com.kratos.module.dictionary.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@ApiModel("字典类别")
public class DictionaryCategory extends BaseEntity {
    @ApiModelProperty(value = "上级类别")
    @ManyToOne(fetch = FetchType.EAGER)
    private DictionaryCategory parent;
    @ApiModelProperty("类别名称")
    @Column(length = 100)
    private String name;
    @ApiModelProperty("类别代码")
    @Column(length = 100)
    private String code;

    public DictionaryCategory getParent() {
        return parent;
    }

    public void setParent(DictionaryCategory parent) {
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
}
