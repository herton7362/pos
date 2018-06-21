package com.kratos.kits;

import com.kratos.kits.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 提供一些封装好的组件，这些组件可能需要使用 {@link Configuration} 进行配置
 * @author tang he
 * @since 1.0.0
 */
@Component
public class Kits {

    private final Notification notification;

    /**
     * 消息组件，可以发送通知消息
     * @return {@link Notification}
     */
    public Notification notification() {
        return notification;
    }

    @Autowired
    public Kits(Notification notificationKit) {
        this.notification = notificationKit;
    }
}
