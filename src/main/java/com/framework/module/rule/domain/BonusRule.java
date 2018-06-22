package com.framework.module.rule.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@ApiModel("收益规则")
public class BonusRule extends BaseEntity {
    @ApiModelProperty(value = "条件如 > 4000 则填写 4000")
    @Column(length = 11, precision = 2)
    private Double condition;
    @ApiModelProperty(value = "备注")
    @Column(length = 100)
    private String remark;

    public Double getCondition() {
        return condition;
    }

    public void setCondition(Double condition) {
        this.condition = condition;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
