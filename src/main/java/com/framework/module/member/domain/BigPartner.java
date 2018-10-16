package com.framework.module.member.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/6/27 13:05
 */
public class BigPartner {
    public static final double BIG_THRESHOLD = 10000000D;

    @ApiModelProperty(value = "盟友ID")
    private String memberId;
    @ApiModelProperty(value = "盟友姓名")
    private String memberName;
    @ApiModelProperty(value = "盟友电话")
    private String memberMobile;
    @ApiModelProperty(value = "盟友交易额")
    private Double transactionAmount;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
