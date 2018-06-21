package com.kratos.kits.notification.config.annotation.configurer;

import com.kratos.kits.notification.NotificationProvider;
import com.kratos.kits.notification.config.annotation.NotificationBuilder;
import com.kratos.kits.notification.config.annotation.NotificationConfigurerAdapter;
import com.kratos.kits.notification.message.NotificationMessage;

/**
 * 消息类型配置，主要设置当前类型的消息提供商{@link NotificationProvider}
 * @param <B> 当前正在构建的Builder
 * @param <H> 当前的消息类型，用来确定 {@link NotificationProvider} 的具体类型
 */
public abstract class NotificationTypeConfigurer<B extends NotificationBuilder, H extends NotificationMessage> extends NotificationConfigurerAdapter<B> {
    private NotificationProvider<H> notificationProvider;
    @SuppressWarnings("unchecked")
    void setProvider(NotificationProvider<H> notificationProvider) {
        this.notificationProvider = notificationProvider;
    }

    public NotificationProvider<H> getProvider() {
        return notificationProvider;
    }
}
