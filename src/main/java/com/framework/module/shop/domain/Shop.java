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
    private Status status = Status.UN_ACTIVE;
    @ApiModelProperty(value = "交易总额")
    @Column(length = 11, precision = 2)
    private Double transactionAmount;
    @ApiModelProperty(value = "是否领取激活奖励0未领取，1已领取")
    @Column(length = 1)
    private Integer activationReward;
    @ApiModelProperty(value = "所属会员是否兑换了设备0未兑换，1已兑换，2兑换审核中")
    @Column(length = 1)
    private Integer exchangePosMachine = 0;
    @Transient
    private Integer activity;
    @Transient
    private String access_token;

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

    public Integer getExchangePosMachine() {
        return exchangePosMachine;
    }

    public void setExchangePosMachine(Integer exchangePosMachine) {
        this.exchangePosMachine = exchangePosMachine;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
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
