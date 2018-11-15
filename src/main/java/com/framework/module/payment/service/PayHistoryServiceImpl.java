package com.framework.module.payment.service;

import com.framework.module.klt.PaymentDemo;
import com.framework.module.klt.model.PaymentContent;
import com.framework.module.member.domain.MemberCashInRecords;
import com.framework.module.member.service.MemberCashInRecordsService;
import com.framework.module.payment.domain.PayHistory;
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

@Component
@Transactional
public class PayHistoryServiceImpl extends AbstractCrudService<PayHistory> implements PayHistoryService {
    private final MemberCashInRecordsService memberCashInRecordsService;

    public PayHistoryServiceImpl(MemberCashInRecordsService memberCashInRecordsService) {
        this.memberCashInRecordsService = memberCashInRecordsService;
    }

    @Override
    public void pay(String cashInId) throws Exception {
        MemberCashInRecords memberCashInRecords = memberCashInRecordsService.findOne(cashInId);
        if (memberCashInRecords == null) {
            throw new BusinessException("无提现记录");
        }
        if (!MemberCashInRecords.Status.PASS.equals(memberCashInRecords.getStatus())) {
            throw new BusinessException("审核未通过，不能提现");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        PaymentContent content = new PaymentContent();
        content.setMchtOrderNo(memberCashInRecords.getId());
        content.setOrderDateTime(simpleDateFormat.format(new Date()));
        content.setAccountNo(memberCashInRecords.getCollectAccount());
        content.setAccountName(memberCashInRecords.getCollectName());
        content.setAccountType("1");
        content.setBankNo("000000000000");
        content.setBankName(memberCashInRecords.getBankName());
        content.setAmt(String.valueOf(memberCashInRecords.getCashAmount() * 100));
        content.setPurpose("用户提现");
        content.setNotifyUrl("http://aaaa");

        PayHistory payHistory = new PayHistory();

        PaymentDemo.sendPayment(content);
    }
}
