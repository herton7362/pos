package com.kratos.kits.notification.provider;

import com.kratos.kits.notification.NotificationProvider;
import com.kratos.kits.notification.config.annotation.configurer.provider.AlidayuProviderConfigurer;
import com.kratos.kits.notification.message.SmsBroadcastMessage;

/**
 *
 * @author tang he
 * @param <B>
 */
public class SmsBroadcastAlidayuProvider<B extends SmsBroadcastMessage> implements NotificationProvider<B> {
    private AlidayuProviderConfigurer configurer;
    public SmsBroadcastAlidayuProvider(AlidayuProviderConfigurer configurer) {
        this.configurer = configurer;
    }
    @Override
    public boolean send(B message) throws Exception {
        return false;
    }
}
