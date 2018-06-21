package com.kratos.kits.notification;

import com.kratos.kits.notification.message.NotificationMessage;

/**
 * @author tang he
 * @param <T>
 */
public interface NotificationProvider<T extends NotificationMessage> {
     boolean send(T message) throws Exception;
}
