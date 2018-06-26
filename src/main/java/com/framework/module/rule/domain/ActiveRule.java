package com.framework.module.rule.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@ApiModel("收益规则")
public class ActiveRule extends BaseEntity {
    @ApiModelProperty(value = "条件如 > 4000 则填写 4000")
    @Column(length = 11, precision = 2)
    private Double conditionValue;
    @ApiModelProperty(value = "奖励金")
    @Column(length = 11, precision = 2)
    private Double awardMoney;
    @ApiModelProperty(value = "备注")
    @Column(length = 100)
    private String remark;

    public Double getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(Double conditionValue) {
        this.conditionValue = conditionValue;
    }

    public Double getAwardMoney() {
        return awardMoney;
    }

    public void setAwardMoney(Double awardMoney) {
        this.awardMoney = awardMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
