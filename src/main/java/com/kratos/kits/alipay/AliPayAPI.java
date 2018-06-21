package com.kratos.kits.alipay;

import com.alipay.api.AlipayApiException;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AliPayAPI {
    /**
     * App支付请求
     * @param outTradeNo 单号
     * @param amount 金额
     * @param describe 支付描述信息
     */
    ResponseEntity<AliPayResult> getAliPayOrderId(String outTradeNo, String amount, String describe) throws AlipayApiException;

    Boolean verifyPayNotification(Map<String, String> params) throws AlipayApiException;

    /**
     * Web支付请求
     * @param outTradeNo 单号
     * @param amount 金额
     * @param describe 支付描述信息
     */
    String getWebAliPayForm(String outTradeNo, String amount, String describe) throws AlipayApiException;
}
