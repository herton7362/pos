package com.framework.module.rule.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@ApiModel("团建拉人奖励规则")
public class GroupBuildDrawRule extends BaseEntity {
    @ApiModelProperty(value = "拉人数量")
    private Integer memberCount;
    @ApiModelProperty(value = "奖励")
    @Column(length = 11, precision = 2)
    private Double reward;
    @ApiModelProperty(value = "备注")
    @Column(length = 100)
    private String remark;

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
