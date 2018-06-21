package com.kratos.kits.notification.message;

/**
 * 语音验证码消息
 * @author tang he
 * @since 1.0.0
 */
public class VoiceVerifyCodeMessage extends NotificationMessage {
    private String verifyCode; // 验证码
    public VoiceVerifyCodeMessage() {
        this.messageType = NotificationMessageType.VOICE_VERIFY_CODE;
    }
    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
