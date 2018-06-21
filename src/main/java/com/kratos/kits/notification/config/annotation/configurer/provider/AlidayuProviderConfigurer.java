package com.kratos.kits.notification.config.annotation.configurer.provider;

import com.kratos.kits.notification.config.annotation.NotificationBuilder;
import com.kratos.kits.notification.config.annotation.configurer.NotificationProviderConfigurer;

/**
 * 阿里大鱼短信平台配置
 * @author tang he
 * @since 1.0.0
 * @param <B>
 */
public class AlidayuProviderConfigurer<B extends NotificationBuilder> extends NotificationProviderConfigurer<B> {
    private String smsType;
    private String smsFreeSignName;
    private String smsTemplateCode;
    private String serverUrl;
    private String appId;
    private String appSecret;

    public String getSmsType() {
        return smsType;
    }

    public AlidayuProviderConfigurer<B> setSmsType(String smsType) {
        this.smsType = smsType;
        return this;
    }

    public String getSmsFreeSignName() {
        return smsFreeSignName;
    }

    public AlidayuProviderConfigurer<B> setSmsFreeSignName(String smsFreeSignName) {
        this.smsFreeSignName = smsFreeSignName;
        return this;
    }

    public String getSmsTemplateCode() {
        return smsTemplateCode;
    }

    public AlidayuProviderConfigurer<B> setSmsTemplateCode(String smsTemplateCode) {
        this.smsTemplateCode = smsTemplateCode;
        return this;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public AlidayuProviderConfigurer<B> setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public AlidayuProviderConfigurer<B> setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public AlidayuProviderConfigurer<B> setAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }
}
