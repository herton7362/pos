package com.kratos.kits.wechat.config.annotation.configuration;

import com.kratos.kits.wechat.config.annotation.builder.WeChatPayConfig;

/**
 * 微信配置
 */
public interface WeChatAPIConfigurer {
    /**
     * 微信支付配置
     * @param weChatPayConfig 支付配置对象
     */
    void configure(WeChatPayConfig weChatPayConfig);
}
