package com.kratos.kits.notification.message;

/**
 * 手机应用推广消息
 * @author tang he
 * @since 1.0.0
 */
public class MobileAppBroadcastMessage extends NotificationMessage {
    private String message; // 消息

    public MobileAppBroadcastMessage() {
        this.messageType = NotificationMessageType.MOBILE_APP_BROADCAST;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
