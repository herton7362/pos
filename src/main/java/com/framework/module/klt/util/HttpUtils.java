package com.framework.module.klt.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: HttpUtils
 * @Description Http请求工具类
 * @Author liujinjian
 * @Date 2018/8/6 10:15
 * @Version 1.0
 */
public class HttpUtils {

    /**
     * 配置忽略SSL认证
     *
     * @param clientBuilder
     */
    public static void configureHttpClient(HttpClientBuilder clientBuilder) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
                return true;
            }
        }).build();
        //NoopHostNameVerifer 接受任何有效的SSL会话来匹配目标主机
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        clientBuilder.setSSLSocketFactory(sslsf);
    }

    /**
     * @param url         请求地址
     * @param reqeust     请求参数
     * @param isVerifySSL 是否开启SSl认证
     * @return
     * @throws IOException
     */
    public static String sendHttpPostRequest(String url, String reqeust, boolean isVerifySSL) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        if (!isVerifySSL)
            configureHttpClient(httpClientBuilder);
        HttpClient httpClient = httpClientBuilder.build();

        StringEntity requestEntity = new StringEntity(reqeust, "utf-8");
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("content-type", "application/json;charset=UTF-8");
        httpPost.addHeader("Accept", "application/json");
        httpPost.setEntity(requestEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream inputStream = httpEntity.getContent();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String s;
        StringBuilder stringBuilder = new StringBuilder();
        while ((s = bufferedReader.readLine()) != null) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

}
