package com.kratos.kits.notification;

import com.kratos.kits.notification.config.annotation.NotificationBuilder;
import com.kratos.kits.notification.config.annotation.builder.NotificationProviders;
import com.kratos.kits.notification.config.annotation.builder.NotificationTypes;
import com.kratos.kits.notification.config.annotation.configuration.NotificationKitConfigurer;
import com.kratos.kits.notification.config.annotation.configuration.NotificationKitConfigurerAdaptor;
import com.kratos.kits.notification.config.annotation.configurer.NotificationTypeConfigurer;
import com.kratos.kits.notification.config.annotation.configurer.SendConfigurer;
import com.kratos.kits.notification.message.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息发送组件
 * 需要通过{@link NotificationKitConfigurerAdaptor}来进行配置
 * 使用线程池进行发送，保证系统不会因为线程过多而崩溃
 *
 * @author tang he
 * @since 1.0.0
 *
 */
@Component
public final class Notification {
    private final NotificationKitConfigurer notificationConfigurer;
    private final NotificationProviders providerConfigurer = new NotificationProviders();
    private final NotificationTypes notificationTypes = new NotificationTypes(providerConfigurer);
    private final SendConfigurer sendConfigurer = new SendConfigurer();

    /**
     * 发送短信验证码，可以使用
     * {@link SmsVerifyCodeMessage}短信验证码
     * {@link VoiceVerifyCodeMessage}语音验证码
     * {@link SmsBroadcastMessage}短信推广
     * {@link MobileAppBroadcastMessage}手机通知消息
     * @param message 消息类
     * @return 是否发送成功 true 成功/false不成功
     * @throws Exception 处理过程中异常
     */
    public <T extends NotificationMessage> boolean send(T message) throws Exception {
        NotificationProvider<T> provider = getProvider(message);
        return !doFilter(message) || provider.send(message);
    }

    /**
     * 根据消息类型获取通知提供商
     * 提供商在{@link NotificationTypeConfigurer}中配置
     * @param message 消息类，可以获取消息类型
     * @return 消息提供商，用来发送消息
     */
    private <T extends NotificationMessage> NotificationProvider<T> getProvider(T message) throws Exception {
        NotificationTypeConfigurer<NotificationBuilder, T> configurer = notificationTypes.getConfigurer(message.getMessageType().getConfigurerClass());
        return configurer.getProvider();
    }

    /**
     * 执行过滤器
     * @param message 消息类，作为过滤器的参数
     * @return 过滤器判断的结果
     */
    private boolean doFilter(NotificationMessage message) throws Exception {
        return sendConfigurer.getSendFilter().isSendAble(message.getDestUser(), message.getMessageType());
    }

    /**
     * 设置用户自定义配置，配置
     * {@link NotificationProviders} 消息提供商，配置提供商基础信息
     * {@link NotificationTypes} 消息类型配置，用来配置消息类型的提供商
     * {@link SendConfigurer} 发送的配置，可配置过滤器
     */
    private void configure() throws Exception {
        notificationConfigurer.configure(providerConfigurer);
        notificationConfigurer.configure(notificationTypes);
        notificationConfigurer.configure(sendConfigurer);
    }

    @Autowired
    public Notification(NotificationKitConfigurer notificationConfigurer) throws Exception {
        this.notificationConfigurer = notificationConfigurer;
        configure();
    }
}
