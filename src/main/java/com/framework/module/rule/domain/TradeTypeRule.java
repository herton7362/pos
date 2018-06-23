package com.framework.module.rule.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@ApiModel("交易类型")
public class TradeTypeRule extends BaseEntity {
    @ApiModelProperty(value = "手续费用")
    @Column(length = 11, precision = 2)
    private Double procedureFee;
    @ApiModelProperty(value = "备注")
    @Column(length = 100)
    private String remark;

    public Double getProcedureFee() {
        return procedureFee;
    }

    public void setProcedureFee(Double procedureFee) {
        this.procedureFee = procedureFee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
