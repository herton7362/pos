package com.kratos.kits.notification.config.annotation.configurer;

import com.kratos.kits.notification.NotificationProvider;
import com.kratos.kits.notification.config.annotation.NotificationBuilder;
import com.kratos.kits.notification.config.annotation.builder.NotificationProviders;
import com.kratos.kits.notification.message.MobileAppBroadcastMessage;

/**
 * 手机应用推送配置，需要注入 {@link NotificationProviders} 用来获取通知提供商的配置
 * @param <B> 当前正在构建的Builder
 * @param <H> 当前的消息类型，用来确定 {@link NotificationProvider} 的具体类型
 */
public class MobileAppBroadcastConfigurer<B extends NotificationBuilder, H extends MobileAppBroadcastMessage> extends NotificationTypeConfigurer<B, H> {
    private NotificationProviders providerConfigurer;
    public MobileAppBroadcastConfigurer(NotificationProviders providerConfigurer) {
        this.providerConfigurer = providerConfigurer;
    }

}
