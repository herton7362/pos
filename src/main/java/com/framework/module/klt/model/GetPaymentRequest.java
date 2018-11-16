package com.framework.module.klt.model;

/**
 * @ClassName: Request
 * @Description
 * @Author liujinjian
 * @Date 2018/8/6 11:46
 * @Version 1.0
 */
public class GetPaymentRequest {

    private Head head;
    private GetPaymentContent content;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public GetPaymentContent getContent() {
        return content;
    }

    public void setContent(GetPaymentContent content) {
        this.content = content;
    }
}
