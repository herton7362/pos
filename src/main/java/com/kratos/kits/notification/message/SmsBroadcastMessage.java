package com.kratos.kits.notification.message;

/**
 * 短信推广消息
 * @author tang he
 * @since 1.0.0
 */
public class SmsBroadcastMessage extends NotificationMessage {
    private String message; // 消息
    public SmsBroadcastMessage() {
        this.messageType = NotificationMessageType.SMS_BROADCAST;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
