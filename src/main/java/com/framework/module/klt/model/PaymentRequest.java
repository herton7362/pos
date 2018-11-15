package com.framework.module.klt.model;

/**
 * @ClassName: Request
 * @Description
 * @Author liujinjian
 * @Date 2018/8/6 11:46
 * @Version 1.0
 */
public class PaymentRequest {

    private Head head;
    private PaymentContent content;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public PaymentContent getContent() {
        return content;
    }

    public void setContent(PaymentContent content) {
        this.content = content;
    }
}
