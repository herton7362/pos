package com.kratos.kits.notification.message;

import com.kratos.kits.notification.config.annotation.NotificationConfigurer;
import com.kratos.kits.notification.config.annotation.configurer.*;

/**
 * @author tang he
 * @since 1.0.0
 */
public enum NotificationMessageType {
    SMS_VERIFY_CODE(SmsVerifyCodeConfigurer.class),
    VOICE_VERIFY_CODE(VoiceVerifyCodeConfigurer.class),
    SMS_BROADCAST(SmsBroadcastConfigurer.class),
    MOBILE_APP_BROADCAST(MobileAppBroadcastConfigurer.class);

    private Class<? extends NotificationConfigurer> configurerClass;

    <C extends NotificationTypeConfigurer> NotificationMessageType(Class<C> clazz) {
        this.configurerClass = clazz;
    }

    /**
     * 为了从消息提供商中获取对应的提供商类
     * @param <C> {@link NotificationConfigurer}的子类
     * @return {@link NotificationConfigurer}
     */
    @SuppressWarnings("unchecked")
    public <C extends NotificationConfigurer> Class<C> getConfigurerClass() {
        return (Class<C>) configurerClass;
    }
}
