package com.kratos.kits.notification.config.annotation.builder;

import com.kratos.kits.notification.config.annotation.AbstractNotificationBuilder;
import com.kratos.kits.notification.config.annotation.configurer.provider.AlidayuProviderConfigurer;
import com.kratos.kits.notification.config.annotation.configurer.provider.IpyyProviderConfigurer;

/**
 * 消息提供商构建
 *
 *  * <pre>
 * &#064;Configuration
 * &#064;EnableWebSecurity
 * public class NotificationConfig extends NotificationKitConfigurerAdaptor {
 *
 * 	&#064;Override
 * 	public void configure(NotificationProviders notificationProvider) throws Exception {
 * 		notificationProvider
 *           .alidayuProvider()
 *           .setAppId("24495956")
 *           .setAppSecret("895912d66f8740c478c967b2167019fb")
 *           .setServerUrl("http://gw.api.taobao.com/router/rest")
 *           .setSmsFreeSignName("威廉小院")
 *           .setSmsTemplateCode("SMS_71820364")
 *           .setSmsType("normal");
 * 	}
 *
 * 	&#064;Override
 * 	public void configure(NotificationTypes notificationTypes) throws Exception {
 * 		notificationTypes
 *           .smsVerifyCode().alidayuProvider()
 *           .and()
 *           .smsBroadcast().alidayuProvider()
 *           .and()
 *           .voiceVerifyCode().alidayuProvider();
 * 	}
 * }
 * </pre>
 * @author tang he
 * @since 1.0.0
 */
public class NotificationProviders extends AbstractNotificationBuilder<NotificationProviders> {
    /**
     * 阿里大鱼短信平台配置
     * @return 阿里大鱼短信平台配置类
     */
    public AlidayuProviderConfigurer<NotificationProviders> alidayuProvider() throws Exception {
        return getOrApply(new AlidayuProviderConfigurer<NotificationProviders>());
    }

    /**
     * ipyy短信平台配置
     * @return ipyy短信平台配置类
     */
    public IpyyProviderConfigurer<NotificationProviders> ipyyProvider() throws Exception {
        return getOrApply(new IpyyProviderConfigurer<NotificationProviders>());
    }
}
