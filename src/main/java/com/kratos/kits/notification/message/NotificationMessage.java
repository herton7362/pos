package com.kratos.kits.notification.message;

import com.kratos.entity.BaseUser;

/**
 * 通知消息类，定义消息基本属性
 * @author tang he
 * @since 1.0.0
 */
public abstract class NotificationMessage {
    private BaseUser destUser; // 目标用户
    NotificationMessageType messageType;// 消息类型

    public BaseUser getDestUser() {
        return destUser;
    }

    public void setDestUser(BaseUser destUser) {
        this.destUser = destUser;
    }

    public NotificationMessageType getMessageType() {
        return messageType;
    }
}
