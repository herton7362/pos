package com.kratos.kits.notification.message;

/**
 * 短信验证码消息
 * @author tang he
 * @since 1.0.0
 */
public class SmsVerifyCodeMessage extends NotificationMessage {
    private String verifyCode; // 验证码
    public SmsVerifyCodeMessage() {
        this.messageType = NotificationMessageType.SMS_VERIFY_CODE;
    }
    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
