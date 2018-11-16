package com.framework.module.payment.domain;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/11/16 13:35
 */
public class PayResult {
    private String resultCode;
    private String resultDes;

    public PayResult(String resultCode, String resultDes) {
        this.resultCode = resultCode;
        this.resultDes = resultDes;
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
}
