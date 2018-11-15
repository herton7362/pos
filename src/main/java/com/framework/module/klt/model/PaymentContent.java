package com.framework.module.klt.model;

/**
 * @ClassName: Content
 * @Description 报文体
 * @Author liujinjian
 * @Date 2018/8/6 10:11
 * @Version 1.0
 */
public class PaymentContent {

    String mchtOrderNo;
    String orderDateTime;
    String accountNo;
    String accountName;
    String accountType;
    String bankNo;
    String bankName;
    String amt;
    String purpose;
    String remark;
    String notifyUrl;

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
}
