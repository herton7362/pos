package com.kratos.kits.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.kratos.common.utils.StringUtils;
import com.kratos.kits.alipay.annotation.builder.AliPayConfig;
import com.kratos.kits.alipay.annotation.configuration.AliPayAPIConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AliPayAPIImpl implements AliPayAPI {
    private final Logger LOG = LoggerFactory.getLogger(AliPayAPIImpl.class);
    private final AliPayConfig aliPayConfig = new AliPayConfig();
    @Override
    public ResponseEntity<AliPayResult> getAliPayOrderId(String outTradeNo, String amount, String describe) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                aliPayConfig.getAppId(), aliPayConfig.getPrivateKey(), "json", "UTF-8", aliPayConfig.getAppPublicKey(), "RSA2");
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(describe);
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount(amount);
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        if(response.isSuccess()) {
            AliPayResult aliPayResult = new AliPayResult();
            aliPayResult.setBody(response.getBody());
            return new ResponseEntity<>(aliPayResult, HttpStatus.OK);
        } else {
            LOG.error(response.getSubMsg());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Boolean verifyPayNotification(Map<String, String> params) throws AlipayApiException {
        String out_trade_no = params.get("out_trade_no");
        System.out.println("********************** 支付宝【订单】支付回调，订单【" + out_trade_no + "】，verify开始 **********************");
        boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPayConfig.getAlipayPublicKey(), "UTF-8", "RSA2");
        boolean tag = false;
        System.out.println("支付宝【订单】支付回调，订单【" + out_trade_no + "】，signVerified，值" + signVerified);
        if(signVerified) {
            // WAIT_BUYER_PAY	交易创建，等待买家付款。
            // TRADE_CLOSED		在指定时间段内未支付时关闭的交易；在交易完成全额退款成功时关闭的交易。
            // TRADE_SUCCESS	交易成功，且可对该交易做操作，如：多级分润、退款等。
            // TRADE_FINISHED	交易成功且结束，即不可再做任何操作。
            String trade_status = params.get("trade_status");
            if(StringUtils.isBlank(trade_status)) {
                tag = false;
            } else if("WAIT_BUYER_PAY".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)) {
                tag = false;
            } else if("TRADE_SUCCESS".equals(trade_status)) {
                tag = true;
            } else if("TRADE_CLOSED".equals(trade_status)) {
                tag = true;
            }
            if(tag) {
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", aliPayConfig.getAppId(), aliPayConfig.getPrivateKey(), "json", "UTF-8", aliPayConfig.getAlipayPublicKey(), "RSA2");
                AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
                request.setBizContent("{" +
                        "\"out_trade_no\":\"" + params.get("out_trade_no") + "\"," +
                        "\"trade_no\":\"" + params.get("trade_no") + "\"" +
                        "}");
                AlipayTradeQueryResponse response = alipayClient.execute(request);
                System.out.println("支付宝【订单】支付回调，订单【" + out_trade_no + "】，response.isSuccess，值" + response.isSuccess());
                System.out.println("支付宝【订单】支付回调，订单【" + out_trade_no + "】，response.getTradeStatus，值" + response.getTradeStatus());
                tag = response.isSuccess() && response.getTradeStatus().equals(params.get("trade_status"));
            }
        }
        return tag;
    }

    @Override
    public String getWebAliPayForm(String outTradeNo, String amount, String describe) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", aliPayConfig.getAppId(), aliPayConfig.getPrivateKey(), "json", "UTF-8", aliPayConfig.getAppPublicKey(), "RSA2"); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(aliPayConfig.getReturnUrl());
        alipayRequest.setNotifyUrl(aliPayConfig.getNotifyUrl());//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\""+outTradeNo+"\"," +
                " \"total_amount\":\""+amount+"\"," +
                " \"subject\":\""+describe+"\"," +
                " \"product_code\":\"QUICK_WAP_PAY\"" +
                " }");//填充业务参数
        return alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
    }

    @Autowired
    public AliPayAPIImpl(AliPayAPIConfigurer aliPayAPIConfigurer) {
        aliPayAPIConfigurer.configure(aliPayConfig);
    }
}
