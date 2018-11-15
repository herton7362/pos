package com.framework.module.klt;

import com.alibaba.fastjson.JSONObject;
import com.framework.module.klt.model.Content;
import com.framework.module.klt.model.Head;
import com.framework.module.klt.model.Request;
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
public class AgreementPaymentDemo {

    //测试地址
    public static final String url = "https://ipay.chinasmartpay.cn/openapi/agreementPayment/getAgreementSms";
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
        Content content = new Content();
        content.setPayerAcctNo("6228480661541889316");
        content.setPayerIdNo("350322199003033832");
        content.setPayerIdType("01");
        content.setPayerName("徐志坚");
        content.setPayerTelephone("13262950632");
        //请求流水号 生成规则请商户自行定义！！！
        content.setRequestId(String.valueOf(System.currentTimeMillis()));
        Request request = new Request();
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
