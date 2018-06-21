package com.kratos.kits.alipay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "支付宝")
public class AliPayResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "支付宝支付信息", name = "body", dataType="string")
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
