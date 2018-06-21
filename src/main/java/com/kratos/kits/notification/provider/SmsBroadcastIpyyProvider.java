package com.kratos.kits.notification.provider;

import com.kratos.kits.notification.NotificationProvider;
import com.kratos.kits.notification.config.annotation.configurer.provider.IpyyProviderConfigurer;
import com.kratos.kits.notification.message.SmsBroadcastMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SmsBroadcastIpyyProvider<B extends SmsBroadcastMessage> extends AbstractIpyyProvider<B> implements NotificationProvider<B> {
    private final Log logger = LogFactory.getLog(this.getClass());
    private IpyyProviderConfigurer configurer;
    public SmsBroadcastIpyyProvider(IpyyProviderConfigurer configurer) {
        this.configurer = configurer;
    }
    @Override
    public boolean send(B message) throws Exception {
        HttpClient httpclient = new SmsBroadcastIpyyProvider.SSLClient();
        String content =  "【" + configurer.getSmsFreeSignName() + "】 " + message.getMessage();
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
