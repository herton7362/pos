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
//    public static final String url = "https://ipay.chinasmartpay.cn/openapi/singlePayment/payment";
//    public static final String get_url = "https://ipay.chinasmartpay.cn/openapi/singlePayment/query";
    //测试md5Key
//    public static final String md5Key = "742fa3ffd050fb441763bf8fb6c0594f";
    //测试商户号
//    public static final String merchantId = "903110153110001";

    // 生产配置
    public static final String url = "https://openapi.openepay.com/openapi/singlePayment/payment";
    public static final String get_url = "https://openapi.openepay.com/openapi/singlePayment/query";
    public static final String md5Key = "MIIEyTCCA7GgAwIBAgIFQSN5BzcwDQYJKoZIhvcNAQELBQAwWDELMAkGA1UEBhMCQ04xMDAuBgNVBAoMJ0NoaW5hIEZpbmFuY2lhbCBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEXMBUGA1UEAwwOQ0ZDQSBBQ1MgT0NBMzEwHhcNMTgwNzA0MTQxOTQ0WhcNMTkwNzA0MTQxOTQyWjB4MQswCQYDVQQGEwJDTjEUMBIGA1UECgwLQ0ZDQSBKVEwgQ0ExDzANBgNVBAsMBkpUTCBSQTEZMBcGA1UECwwQT3JnYW5pemF0aW9uYWwtMTEnMCUGA1UEAwweMDUxQDkwMzMxMDExMjM0MDAwMUBaUkQ1QFNlcTEwMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlPcIZB8swpLoPR2u2JgYnEJ/J6d9hZsRBBQP9grAulrWm3ZD8RE7QOCtp8D580paAOPTl735tInCSUjhV++cauByvXmmkyNeq3lshPCxFY0unWxCJQ1vLxOrsQCcQe5Nod/jYwwjunO3YF5hRPM+m9m9BOXR8svQpU3wc7xCCBYTcw+Q5xMomIoBXiDrCU3awrHd2OAsGA25FO/AoXH9j1xF+yTOQ6P0aX1uralM1XZOEwEhh+kepos+146GHqHIx46Zi/y5f77Zf0YroaiCnCCPE7NT7c/5x8F0TAz9gK8rpG6oKfRB/j/RAK0wG94UlSJy/7a6YiCsv03ydV4IwQIDAQABo4IBeDCCAXQwbAYIKwYBBQUHAQEEYDBeMCgGCCsGAQUFBzABhhxodHRwOi8vb2NzcC5jZmNhLmNvbS5jbi9vY3NwMDIGCCsGAQUFBzAChiZodHRwOi8vY3JsLmNmY2EuY29tLmNuL29jYTMxL29jYTMxLmNlcjAfBgNVHSMEGDAWgBTitAnLzWGhc0p5f/GKgwvdtH6MHTAMBgNVHRMBAf8EAjAAMEgGA1UdIARBMD8wPQYIYIEchu8qAQQwMTAvBggrBgEFBQcCARYjaHR0cDovL3d3dy5jZmNhLmNvbS5jbi91cy91cy0xNC5odG0wPQYDVR0fBDYwNDAyoDCgLoYsaHR0cDovL2NybC5jZmNhLmNvbS5jbi9vY2EzMS9SU0EvY3JsMTI0Ny5jcmwwDgYDVR0PAQH/BAQDAgbAMB0GA1UdDgQWBBTEhkHbFTFbh+SAv0CAV97nNeF5OTAdBgNVHSUEFjAUBggrBgEFBQcDAgYIKwYBBQUHAwQwDQYJKoZIhvcNAQELBQADggEBANd8DEXNPzBl+Acx65y0XEoAJqFta4rog3tuSk6ZDsh9CxdUPNihOT9s7u4xGEG6T5QCe90ZJuvWb6WZN/oKxL8iJRblWBilkmXMpAYCwev/6yPFeNPO7s7Uhoci3eoQPC6FfzEUN/z6EoRgQ+L73ilpLRZbmhIvz1GKO64dR4jcJcS75/BcJQNoJn3geRbySzGN2tKHWgM73jkWM0X/D+2+cn8b/xsYfoCWB36ShIc1OfPTK9ntKQGcWCAOYTaiK+s93xVBMb97RYmj48Ew8ocOlLM3Y0t3TA4K0E2XtSCa7IRW6xz6FTk+x+5BtytMg/3XVmR56YaDa/n6mm24d6I=";
    public static final String merchantId = "903210118110801";

    /**
     * 扣费接口
     *
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
