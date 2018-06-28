package com.framework.config;

import com.kratos.kits.notification.config.annotation.builder.NotificationProviders;
import com.kratos.kits.notification.config.annotation.builder.NotificationTypes;
import com.kratos.kits.notification.config.annotation.configuration.NotificationKitConfigurerAdaptor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig extends NotificationKitConfigurerAdaptor {
    @Override
    public void configure(NotificationProviders notificationProvider) throws Exception {
        notificationProvider
                .alidayuProvider()
                .setAppId("LTAIhzaeNgs34p4B")
                .setAppSecret("e7GPlgoluQ9SUxbuZLL9a1Y1LxdzNJ")
                .setServerUrl("http://gw.api.taobao.com/router/rest")
                .setSmsFreeSignName("会员宝管家")
                .setSmsTemplateCode("SMS_138370042")
                .setSmsType("normal")
                .and()
                .ipyyProvider()
                .setServerUrl("https://dx.ipyy.net/sms.aspx")
                .setAccountName("KA00012")
                .setPassword("KA00012789")
                .setSmsFreeSignName("鼎骏出行");
    }

    @Override
    public void configure(NotificationTypes notificationTypes) throws Exception {
        notificationTypes
                .smsVerifyCode().alidayuProvider()
                .and()
                .smsBroadcast().ipyyProvider()
                .and()
                .voiceVerifyCode().alidayuProvider();
    }
}
