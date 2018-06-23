package com.framework.module.rule.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@ApiModel("团建最低采购数设置")
public class GroupBuildPurchaseRule extends BaseEntity {
    @ApiModelProperty(value = "最低采购数")
    private Integer minPurchaseCount;
    @ApiModelProperty(value = "备注")
    @Column(length = 100)
    private String remark;

    public Integer getMinPurchaseCount() {
        return minPurchaseCount;
    }

    public void setMinPurchaseCount(Integer minPurchaseCount) {
        this.minPurchaseCount = minPurchaseCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
