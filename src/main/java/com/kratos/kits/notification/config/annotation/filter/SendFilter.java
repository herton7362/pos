package com.kratos.kits.notification.config.annotation.filter;

import com.kratos.entity.BaseUser;
import com.kratos.kits.notification.message.NotificationMessageType;

/**
 * 发送通知的过滤器
 */
public interface SendFilter {
    
    /**
     * 是否可以发送
     *
     * @param destUser 要发送给的用户
     * @param messageType 消息类型
     * @return 是否可以发送 true可以/false不可以
     */
    boolean isSendAble(BaseUser destUser, NotificationMessageType messageType) throws Exception;
}
