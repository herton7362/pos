package com.kratos.kits.notification.config.annotation.configuration;

import com.kratos.entity.BaseUser;
import com.kratos.kits.notification.config.annotation.configurer.SendConfigurer;
import com.kratos.kits.notification.message.NotificationMessageType;

public abstract class NotificationKitConfigurerAdaptor implements NotificationKitConfigurer {

    @Override
    public void configure(SendConfigurer sendConfigurer) throws Exception {
        sendConfigurer.setSendFilter((BaseUser destUser, NotificationMessageType messageType) -> true);
    }

}
