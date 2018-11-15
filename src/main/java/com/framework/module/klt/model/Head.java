package com.framework.module.klt.model;

/**
 * @ClassName: Head
 * @Description 报文头
 * @Author liujinjian
 * @Date 2018/8/6 10:07
 * @Version 1.0
 */
public class Head {

    //版本号默认18
    private String version;
    //签名类型
    private String signType;
    //商户号
    private String merchantId;
    //交易类型
    private String transactType;
    //签名串
    private String sign;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTransactType() {
        return transactType;
    }

    public void setTransactType(String transactType) {
        this.transactType = transactType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
