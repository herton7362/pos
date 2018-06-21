package com.kratos.kits.notification.config.annotation.builder;

import com.kratos.kits.notification.config.annotation.AbstractNotificationBuilder;
import com.kratos.kits.notification.config.annotation.configurer.MobileAppBroadcastConfigurer;
import com.kratos.kits.notification.config.annotation.configurer.SmsBroadcastConfigurer;
import com.kratos.kits.notification.config.annotation.configurer.SmsVerifyCodeConfigurer;
import com.kratos.kits.notification.config.annotation.configurer.VoiceVerifyCodeConfigurer;
import com.kratos.kits.notification.message.MobileAppBroadcastMessage;
import com.kratos.kits.notification.message.SmsBroadcastMessage;
import com.kratos.kits.notification.message.SmsVerifyCodeMessage;
import com.kratos.kits.notification.message.VoiceVerifyCodeMessage;

/**
 * 消息类型构建
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
public class NotificationTypes extends AbstractNotificationBuilder<NotificationTypes> {
    private NotificationProviders providerConfigurer;
    private NotificationTypes() {}
    public NotificationTypes(NotificationProviders providerConfigurer) {
        this.providerConfigurer = providerConfigurer;
    }
    public SmsVerifyCodeConfigurer<NotificationTypes, SmsVerifyCodeMessage> smsVerifyCode() throws Exception {
        return getOrApply(new SmsVerifyCodeConfigurer<NotificationTypes, SmsVerifyCodeMessage>(providerConfigurer));
    }

    public VoiceVerifyCodeConfigurer<NotificationTypes, VoiceVerifyCodeMessage> voiceVerifyCode() throws Exception {
        return getOrApply(new VoiceVerifyCodeConfigurer<NotificationTypes, VoiceVerifyCodeMessage>(providerConfigurer));
    }

    public SmsBroadcastConfigurer<NotificationTypes, SmsBroadcastMessage> smsBroadcast() throws Exception {
        return getOrApply(new SmsBroadcastConfigurer<NotificationTypes, SmsBroadcastMessage>(providerConfigurer));
    }

    public MobileAppBroadcastConfigurer<NotificationTypes, MobileAppBroadcastMessage> mobileAppBroadcast() throws Exception {
        return getOrApply(new MobileAppBroadcastConfigurer<NotificationTypes, MobileAppBroadcastMessage>(providerConfigurer));
    }
}
