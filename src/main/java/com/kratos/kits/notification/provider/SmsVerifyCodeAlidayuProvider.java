package com.kratos.kits.notification.provider;

import com.kratos.kits.notification.NotificationProvider;
import com.kratos.kits.notification.config.annotation.configurer.provider.AlidayuProviderConfigurer;
import com.kratos.kits.notification.message.SmsVerifyCodeMessage;

/**
 *
 * @author tang he
 * @param <B>
 */
public class SmsVerifyCodeAlidayuProvider<B extends SmsVerifyCodeMessage> implements NotificationProvider<B> {
    private AlidayuProviderConfigurer configurer;
    public SmsVerifyCodeAlidayuProvider(AlidayuProviderConfigurer configurer) {
        this.configurer = configurer;
    }
    @Override
    public boolean send(B message) throws Exception {
        System.out.println("短信 配置:smsType=" + this.configurer.getSmsType());
        System.out.println("smsFreeSignName=" + this.configurer.getSmsFreeSignName());
        System.out.println("smsTemplateCode=" + this.configurer.getSmsTemplateCode());
        System.out.println("serverUrl=" + this.configurer.getServerUrl());
        System.out.println("appId=" + this.configurer.getAppId());
        System.out.println("appSecret=" + this.configurer.getAppSecret());
        System.out.println("发送短信 dest:" + message.getDestUser() + " code:" + message.getVerifyCode());
        return false;
    }
}
