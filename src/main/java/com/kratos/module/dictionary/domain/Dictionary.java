package com.kratos.module.dictionary.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@ApiModel("数据字典")
public class Dictionary extends BaseEntity {
    @ApiModelProperty(value = "字典类别")
    @ManyToOne(fetch = FetchType.EAGER)
    private DictionaryCategory dictionaryCategory;
    @ApiModelProperty("字典名称")
    @Column(length = 100)
    private String name;
    @ApiModelProperty("字典代码")
    @Column(length = 100)
    private String code;

    public DictionaryCategory getDictionaryCategory() {
        return dictionaryCategory;
    }

    public void setDictionaryCategory(DictionaryCategory dictionaryCategory) {
        this.dictionaryCategory = dictionaryCategory;
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
