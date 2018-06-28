package com.kratos.kits.notification.provider;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.kratos.exceptions.BusinessException;
import com.kratos.kits.notification.NotificationProvider;
import com.kratos.kits.notification.config.annotation.configurer.provider.AlidayuProviderConfigurer;
import com.kratos.kits.notification.message.SmsVerifyCodeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author tang he
 * @param <B>
 */
public class SmsVerifyCodeAlidayuProvider<B extends SmsVerifyCodeMessage> implements NotificationProvider<B> {
    private final Log logger = LogFactory.getLog(this.getClass());
    private AlidayuProviderConfigurer configurer;
    public SmsVerifyCodeAlidayuProvider(AlidayuProviderConfigurer configurer) {
        this.configurer = configurer;
    }
    @Override
    public boolean send(B message) throws Exception {
        logger.info("短信 配置:smsType=" + this.configurer.getSmsType());
        logger.info("smsFreeSignName=" + this.configurer.getSmsFreeSignName());
        logger.info("smsTemplateCode=" + this.configurer.getSmsTemplateCode());
        logger.info("serverUrl=" + this.configurer.getServerUrl());
        logger.info("appId=" + this.configurer.getAppId());
        logger.info("appSecret=" + this.configurer.getAppSecret());
        logger.info("发送短信 dest:" + message.getDestUser() + " code:" + message.getVerifyCode());

        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", this.configurer.getAppId(),
                this.configurer.getAppSecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(message.getDestUser().getMobile());
        request.setSignName(this.configurer.getSmsFreeSignName());
        request.setTemplateCode(this.configurer.getSmsTemplateCode());
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam("{\"code\":\""+message.getVerifyCode()+"\"}");
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            return true;
        } else {
            throw new BusinessException("短信发送失败请联系管理员");
        }
    }
}
