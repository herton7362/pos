package com.framework.module.member.domain;

import com.kratos.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;

/**
 * <p>Description: 提现记录表</p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/6/23 11:30
 */
@Entity
public class MemberCashInRecords extends BaseEntity {

    @ApiModelProperty(value = "会员ID")
    @Column(length = 50)
    private String memberId;

    @ApiModelProperty(value = "提现金额")
    @Column(length = 11, precision = 2)
    private Double cashAmount;

    @ApiModelProperty(value = "审核状态")
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ApiModelProperty(value = "提现失败理由")
    @Column(length = 20)
    private String reason;

    @ApiModelProperty(value = "收款银行卡卡号")
    @Column(length = 20)
    private String collectAccount;

    @ApiModelProperty(value = "收款人")
    @Column(length = 20)
    private String collectName;

    @ApiModelProperty(value = "流水单号")
    @Column(length = 20)
    private String serialNum;

    @ApiModelProperty(value = "银行卡银行")
    @Column(length = 20)
    private String bankName;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCollectAccount() {
        return collectAccount;
    }

    public void setCollectAccount(String collectAccount) {
        this.collectAccount = collectAccount;
    }

    public String getCollectName() {
        return collectName;
    }

    public void setCollectName(String collectName) {
        this.collectName = collectName;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public enum Status {
        PENDING("待审核"), PASS("已通过"), UN_PASS("未通过");
        private String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
