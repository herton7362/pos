package com.kratos.kits.notification.provider;

import com.kratos.kits.notification.NotificationProvider;
import com.kratos.kits.notification.config.annotation.configurer.provider.AlidayuProviderConfigurer;
import com.kratos.kits.notification.message.VoiceVerifyCodeMessage;

/**
 *
 * @author tang he
 * @param <B>
 */
public class VoiceVerifyCodeAlidayuProvider<B extends VoiceVerifyCodeMessage> implements NotificationProvider<B> {
    private AlidayuProviderConfigurer configurer;
    public VoiceVerifyCodeAlidayuProvider(AlidayuProviderConfigurer configurer) {
        this.configurer = configurer;
    }
    @Override
    public boolean send(B message) throws Exception {
        return false;
    }
}
