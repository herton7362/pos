package com.framework.module.klt.model;

/**
 * @ClassName: Content
 * @Description 报文体
 * @Author liujinjian
 * @Date 2018/8/6 10:11
 * @Version 1.0
 */
public class Content {

    //付款人姓名
    String payerName;
    //付款人手机号
    String payerTelephone;
    //银行卡号
    String payerAcctNo;
    //身份证号
    String payerIdNo;
    //证件类型
    String payerIdType;
    //请求流水号
    String requestId;

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerTelephone() {
        return payerTelephone;
    }

    public void setPayerTelephone(String payerTelephone) {
        this.payerTelephone = payerTelephone;
    }

    public String getPayerAcctNo() {
        return payerAcctNo;
    }

    public void setPayerAcctNo(String payerAcctNo) {
        this.payerAcctNo = payerAcctNo;
    }

    public String getPayerIdNo() {
        return payerIdNo;
    }

    public void setPayerIdNo(String payerIdNo) {
        this.payerIdNo = payerIdNo;
    }

    public String getPayerIdType() {
        return payerIdType;
    }

    public void setPayerIdType(String payerIdType) {
        this.payerIdType = payerIdType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
