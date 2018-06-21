package com.kratos.kits.notification.config.annotation.configurer.provider;

import com.kratos.kits.notification.config.annotation.NotificationBuilder;
import com.kratos.kits.notification.config.annotation.configurer.NotificationProviderConfigurer;

public class IpyyProviderConfigurer<B extends NotificationBuilder> extends NotificationProviderConfigurer<B> {
    private String smsFreeSignName;
    private String serverUrl;
    private String accountName;
    private String password;

    public String getSmsFreeSignName() {
        return smsFreeSignName;
    }

    public IpyyProviderConfigurer<B> setSmsFreeSignName(String smsFreeSignName) {
        this.smsFreeSignName = smsFreeSignName;
        return this;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public IpyyProviderConfigurer<B> setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    public String getAccountName() {
        return accountName;
    }

    public IpyyProviderConfigurer<B> setAccountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public IpyyProviderConfigurer<B> setPassword(String password) {
        this.password = password;
        return this;
    }
}
