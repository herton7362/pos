package com.framework.module.shop.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@ApiModel("商户")
public class Shop extends BaseEntity {
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "会员id")
    private String memberId;
    @ApiModelProperty(value = "sn码")
    private String sn;
    @ApiModelProperty(value = "手机")
    private String mobile;
    @ApiModelProperty(value = "激活状态")
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Status status;
    @ApiModelProperty(value = "交易总额")
    @Column(length = 11, precision = 2)
    private Double transactionAmount;
    @ApiModelProperty(value = "是否领取激活奖励")
    @Column(length = 1)
    private Integer activationReward;
    @Transient
    private Integer activity;

    public Integer getActivity() {
        return activity;
    }

    public void setActivity(Integer activity) {
        this.activity = activity;
    }

    public Integer getActivationReward() {
        return activationReward;
    }

    public void setActivationReward(Integer activationReward) {
        this.activationReward = activationReward;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public enum Status {
        ACTIVE("已激活"),
        UN_ACTIVE("未激活");
        private String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
