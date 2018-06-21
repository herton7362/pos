package com.kratos.kits.notification.provider;

import com.kratos.kits.notification.NotificationProvider;
import com.kratos.kits.notification.config.annotation.configurer.provider.IpyyProviderConfigurer;
import com.kratos.kits.notification.message.SmsVerifyCodeMessage;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <B>
 */
public class SmsVerifyCodeIpyyProvider<B extends SmsVerifyCodeMessage> extends AbstractIpyyProvider<B> implements NotificationProvider<B> {
    private IpyyProviderConfigurer configurer;
    public SmsVerifyCodeIpyyProvider(IpyyProviderConfigurer configurer) {
        this.configurer = configurer;
    }
    @Override
    public boolean send(B message) throws Exception {
        HttpClient httpclient = new SSLClient();
        String content =  String.format("【%s】 您的手机验证码：%s，5分钟内有效，请勿泄露。如非本人操作，请忽略此短信，谢谢。", configurer.getSmsFreeSignName(), message.getVerifyCode());
        HttpPost post = new HttpPost(configurer.getServerUrl());
        post.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("action","send"));
        nvps.add(new BasicNameValuePair("userid", ""));
        nvps.add(new BasicNameValuePair("account", configurer.getAccountName()));
        nvps.add(new BasicNameValuePair("password", configurer.getPassword()));
        nvps.add(new BasicNameValuePair("mobile", message.getDestUser().getMobile()));		//多个手机号用逗号分隔
        nvps.add(new BasicNameValuePair("content", content));
        nvps.add(new BasicNameValuePair("sendTime", ""));
        nvps.add(new BasicNameValuePair("extno", ""));

        post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));

        sendLog(httpclient.execute(post), content);
        return true;
    }

}
