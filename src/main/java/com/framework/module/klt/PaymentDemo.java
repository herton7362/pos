package com.framework.module.klt;


import com.alibaba.fastjson.JSONObject;
import com.framework.module.klt.model.*;
import com.framework.module.klt.util.HttpUtils;
import com.framework.module.klt.util.SignUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class PaymentDemo {

    //测试地址
    public static final String url = "https://ipay.chinasmartpay.cn/openapi/singlePayment/payment";

    public static final String get_url = "https://ipay.chinasmartpay.cn/openapi/singlePayment/query";
    //测试md5Key
    public static final String md5Key = "742fa3ffd050fb441763bf8fb6c0594f";
    //测试商户号
    public static final String merchantId = "903110153110001";

    /**
     * 扣费接口
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws KeyManagementException
     */
    public static String sendPayment(PaymentContent content) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        Head head = new Head();
        head.setMerchantId(merchantId);
        head.setSignType("1");
        PaymentRequest request = new PaymentRequest();
        request.setHead(head);
        request.setContent(content);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(request);
        String originSign = jsonObject.toJSONString();
        String signStr = SignUtils.addSign(originSign, md5Key);
        return HttpUtils.sendHttpPostRequest(url, signStr, false);
    }

    public static String getPayment(GetPaymentContent content) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        Head head = new Head();
        head.setMerchantId(merchantId);
        head.setSignType("1");
        GetPaymentRequest request = new GetPaymentRequest();
        request.setHead(head);
        request.setContent(content);
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(request);
        String originSign = jsonObject.toJSONString();
        String signStr = SignUtils.addSign(originSign, md5Key);
        return HttpUtils.sendHttpPostRequest(get_url, signStr, false);
    }

}
