package com.framework.module.payment.domain;

import com.kratos.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/11/15 11:26
 */
@Entity
public class PayHistory extends BaseEntity {
    @Column(length = 50)
    private String mchtOrderNo;
    @Column(length = 50)
    private String orderDateTime;
    @Column(length = 50)
    private String accountNo;
    @Column(length = 200)
    private String accountName;
    @Column(length = 50)
    private String accountType;
    @Column(length = 50)
    private String bankNo;
    @Column(length = 200)
    private String bankName;
    @Column(length = 50)
    private String amt;
    @Column(length = 50)
    private String purpose;
    @Column(length = 200)
    private String remark;
    @Column(length = 200)
    private String notifyUrl;
    @Column(length = 50)
    private String requestId;
    @Column(length = 50)
    private String mchtId;
    @Column(length = 50)
    private String resultCode;
    @Column(length = 50)
    private String resultDes;
    @Column(length = 500)
    private String orderState;
    @Column(length = 50)
    private String cashInId;

    public String getCashInId() {
        return cashInId;
    }

    public void setCashInId(String cashInId) {
        this.cashInId = cashInId;
    }

    public String getMchtOrderNo() {
        return mchtOrderNo;
    }

    public void setMchtOrderNo(String mchtOrderNo) {
        this.mchtOrderNo = mchtOrderNo;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMchtId() {
        return mchtId;
    }

    public void setMchtId(String mchtId) {
        this.mchtId = mchtId;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDes() {
        return resultDes;
    }

    public void setResultDes(String resultDes) {
        this.resultDes = resultDes;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }
}
