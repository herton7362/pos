package com.framework.module.klt;


import com.alibaba.fastjson.JSONObject;
import com.framework.module.klt.model.Head;
import com.framework.module.klt.model.PaymentContent;
import com.framework.module.klt.model.PaymentRequest;
import com.framework.module.klt.util.HttpUtils;
import com.framework.module.klt.util.SignUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: AgreementPaymentDemo
 * @Description 协议支付demo
 * @Author liujinjian
 * @Date 2018/8/6 14:28
 * @Version 1.0
 */
public class PaymentDemo {

    //测试地址
    public static final String url = "https://ipay.chinasmartpay.cn/openapi/singlePayment/payment";
    //测试md5Key
    public static final String md5Key = "742fa3ffd050fb441763bf8fb6c0594f";
    //测试商户号
    public static final String merchantId = "903110153110001";


    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        System.out.println(getAgreementSms());
    }


    /**
     * 协议支付签约发短信
     *
     * @return
     * @throws IOException
     */
    public static String getAgreementSms() throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        //参数
        Head head = new Head();
        head.setMerchantId(merchantId);
        head.setSignType("1");
        PaymentContent content = new PaymentContent();
        content.setMchtOrderNo("1234615");
        content.setOrderDateTime("20181115100610");
        content.setAccountNo("6228480661541889316");
        content.setAccountName("大傻");
        content.setAccountType("1");
        content.setBankNo("000000000000");
        content.setBankName("中国银行");
        content.setAmt("100");
        content.setPurpose("扯犊子");
        content.setRemark("aaa");
        content.setNotifyUrl("http://aaaa");
        PaymentRequest request = new PaymentRequest();
        request.setHead(head);
        request.setContent(content);

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(request);
        String originSign = jsonObject.toJSONString();
        //添加签名
        String signStr = SignUtils.addSign(originSign, md5Key);
        //忽略SSL认证
        String respJsonStr = HttpUtils.sendHttpPostRequest(url, signStr, false);

        return respJsonStr;
    }

}
