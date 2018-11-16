package com.framework.module.klt.model;

/**
 * @ClassName: Content
 * @Description 报文体
 * @Author liujinjian
 * @Date 2018/8/6 10:11
 * @Version 1.0
 */
public class GetPaymentContent {

    String mchtOrderNo;
    String paymentBusinessType;
    String orderDate;

    public String getMchtOrderNo() {
        return mchtOrderNo;
    }

    public void setMchtOrderNo(String mchtOrderNo) {
        this.mchtOrderNo = mchtOrderNo;
    }

    public String getPaymentBusinessType() {
        return paymentBusinessType;
    }

    public void setPaymentBusinessType(String paymentBusinessType) {
        this.paymentBusinessType = paymentBusinessType;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
