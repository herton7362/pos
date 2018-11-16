package com.framework.module.payment.service;

import com.alibaba.fastjson.JSONObject;
import com.framework.module.klt.PaymentDemo;
import com.framework.module.klt.model.PaymentContent;
import com.framework.module.member.domain.MemberCashInRecords;
import com.framework.module.member.service.MemberCashInRecordsService;
import com.framework.module.payment.domain.PayHistory;
import com.framework.module.payment.domain.PayResult;
import com.kratos.common.AbstractCrudService;
import com.kratos.exceptions.BusinessException;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class PayHistoryServiceImpl extends AbstractCrudService<PayHistory> implements PayHistoryService {
    private final MemberCashInRecordsService memberCashInRecordsService;

    public PayHistoryServiceImpl(MemberCashInRecordsService memberCashInRecordsService) {
        this.memberCashInRecordsService = memberCashInRecordsService;
    }

    @Override
    public PayResult pay(String cashInId) throws Exception {
        MemberCashInRecords memberCashInRecords = memberCashInRecordsService.findOne(cashInId);
        if (memberCashInRecords == null) {
            throw new BusinessException("无提现记录");
        }
        if (!MemberCashInRecords.Status.PASS.equals(memberCashInRecords.getStatus())) {
            throw new BusinessException("审核未通过，不能提现");
        }

        Map<String, String[]> param = new HashMap<>();
        param.put("mchtOrderNo", new String[]{memberCashInRecords.getId()});
        List<PayHistory> payHistoryList = findAll(param);
        if (payHistoryList.size() != 0) {
            throw new BusinessException("已经支付过，不能重复支付");
        }
        String transaction = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + RandomStringUtils.randomNumeric(4);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        PaymentContent content = new PaymentContent();
        content.setMchtOrderNo(transaction);
        content.setOrderDateTime(simpleDateFormat.format(new Date()));
        content.setAccountNo(memberCashInRecords.getCollectAccount());
        content.setAccountName(memberCashInRecords.getCollectName());
        content.setAccountType("1");
        content.setBankNo("000000000000");
        content.setBankName(memberCashInRecords.getBankName());
        Double cash = memberCashInRecords.getCashAmount() * 100;
        String cashStr = String.valueOf(cash);
        content.setAmt(cashStr.substring(0, cashStr.indexOf(".")));
        content.setPurpose("用户提现");
        content.setRemark("用户提现");
        content.setNotifyUrl("http://aaaa");
        String result = PaymentDemo.sendPayment(content);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String resultCode = jsonObject.getString("responseCode");
        String resultDes = jsonObject.getString("responseMsg");

        PayHistory payHistory = new PayHistory();
        if (jsonObject.containsKey("mchtId")) {
            payHistory.setMchtId(jsonObject.getString("mchtId"));
        }
        if (jsonObject.containsKey("requestId")) {
            payHistory.setRequestId(jsonObject.getString("requestId"));
        }
        if (jsonObject.containsKey("orderState")) {
            payHistory.setOrderState(jsonObject.getString("orderState"));
        }
        payHistory.setMchtOrderNo(content.getMchtOrderNo());
        payHistory.setOrderDateTime(content.getOrderDateTime());
        payHistory.setAccountNo(content.getAccountNo());
        payHistory.setAccountName(content.getAccountName());
        payHistory.setAccountType(content.getAccountType());
        payHistory.setBankNo(content.getBankNo());
        payHistory.setBankName(content.getBankName());
        payHistory.setAmt(content.getAmt());
        payHistory.setPurpose(content.getPurpose());
        payHistory.setNotifyUrl(content.getNotifyUrl());
        payHistory.setResultCode(resultCode);
        payHistory.setResultDes(resultDes);
        payHistory.setCashInId(memberCashInRecords.getId());
        save(payHistory);
        return new PayResult(resultCode, resultDes);
    }

    @Override
    public PayHistory getPayInfo(String paymentId) throws Exception {
        PayHistory payHistory = findOne(paymentId);
        if (payHistory == null) {
            throw new BusinessException("无代付记录");
        }

        return null;
    }
}
