package com.kratos.kits.wechat;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by He on 2017/5/27.
 */
public interface WeChatAPI {
    /**
     * 创建微信支付订单
     * @param orderNumber 订单号
     * @param request HttpServletRequest
     * @param openid 会员openid
     * @param totalFee 支付金额 单位分
     * @return 支付订单参数
     */
    Map<String, Object> makeUnifiedOrder(String orderNumber, HttpServletRequest request, String openid, Integer totalFee) throws Exception;

    /**
     * 创建APP微信支付订单
     * @param orderNumber 订单号
     * @param request HttpServletRequest
     * @param totalFee 支付金额 单位分
     * @return 支付订单参数
     */
    Map<String, Object> makeAppUnifiedOrder(String orderNumber, HttpServletRequest request, Integer totalFee) throws Exception;

    /**
     * 创建WEB微信支付订单
     * @param orderNumber 订单号
     * @param request HttpServletRequest
     * @param totalFee 支付金额 单位分
     * @return 支付订单参数
     */
    Map<String, Object> makeWebUnifiedOrder(String orderNumber, HttpServletRequest request, Integer totalFee) throws Exception;

    /**
     * 获取用户信息
     * @param code 第三方登录返回的code
     * @return 用户信息
     */
    WeChatAPIImpl.UserInfo getUserInfo(String code) throws Exception;
}
