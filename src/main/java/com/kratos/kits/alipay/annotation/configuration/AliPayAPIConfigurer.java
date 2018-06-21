package com.kratos.kits.alipay.annotation.configuration;

import com.kratos.kits.alipay.annotation.builder.AliPayConfig;

public interface AliPayAPIConfigurer {
    /**
     * 支付宝支付配置
     * @param aliPayConfig 支付配置对象
     */
    void configure(AliPayConfig aliPayConfig);
}
