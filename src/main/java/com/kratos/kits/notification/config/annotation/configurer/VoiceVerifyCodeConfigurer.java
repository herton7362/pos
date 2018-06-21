package com.kratos.kits.notification.config.annotation.configurer;

import com.kratos.kits.notification.NotificationProvider;
import com.kratos.kits.notification.config.annotation.NotificationBuilder;
import com.kratos.kits.notification.config.annotation.builder.NotificationProviders;
import com.kratos.kits.notification.config.annotation.configurer.provider.AlidayuProviderConfigurer;
import com.kratos.kits.notification.message.VoiceVerifyCodeMessage;
import com.kratos.kits.notification.provider.VoiceVerifyCodeAlidayuProvider;

/**
 * 语音验证码配置，需要注入 {@link NotificationProviders} 用来获取通知提供商的配置
 * @param <B> 当前正在构建的Builder
 * @param <H> 当前的消息类型，用来确定 {@link NotificationProvider} 的具体类型
 */
public class VoiceVerifyCodeConfigurer<B extends NotificationBuilder, H extends VoiceVerifyCodeMessage> extends NotificationTypeConfigurer<B, H> {
    private NotificationProviders providerConfigurer;
    public VoiceVerifyCodeConfigurer(NotificationProviders providerConfigurer) {
        this.providerConfigurer = providerConfigurer;
    }

    /**
     * 配置为阿里大鱼通知提供商
     * @return {@link VoiceVerifyCodeConfigurer}
     */
    public VoiceVerifyCodeConfigurer<B, H> alidayuProvider() throws Exception {
        this.setProvider(new VoiceVerifyCodeAlidayuProvider<>(providerConfigurer.getConfigurer(AlidayuProviderConfigurer.class)));
        return this;
    }
}
