package com.framework.module.member.service;

import com.framework.module.member.domain.Member;
import com.framework.module.member.domain.MemberCashInRecords;
import com.framework.module.member.domain.MemberCashInRecordsRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.utils.StringUtils;
import com.kratos.exceptions.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 提现业务实现类
 */
@Component("MemberCashInRecordsService")
@Transactional
public class MemberCashInRecordsServiceImpl extends AbstractCrudService<MemberCashInRecords> implements MemberCashInRecordsService {
    private final MemberService memberService;
    private final MemberCashInRecordsRepository memberCashInRecordsRepository;

    public MemberCashInRecordsServiceImpl(MemberService memberService, MemberCashInRecordsRepository memberCashInRecordsRepository) {
        this.memberService = memberService;
        this.memberCashInRecordsRepository = memberCashInRecordsRepository;
    }

    @Override
    public boolean cashIn(String memberId, Double cashOnAmount) throws Exception {
        Member member = memberService.findOne(memberId);
        if (member == null) {
            throw new BusinessException(String.format("会员id:[%s]不存在", memberId));
        }
        if (StringUtils.isBlank(member.getOpenAccountBank()) || StringUtils.isBlank(member.getBankCardNumber())) {
            throw new BusinessException("请设置提现银行卡信息");
        }
        MemberCashInRecords record = new MemberCashInRecords();
        record.setMemberId(memberId);
        record.setCashAmount(cashOnAmount);
        record.setStatus(MemberCashInRecords.Status.PENDING);
        record.setCollectAccount(member.getBankCardNumber());
        record.setCollectName(member.getName());
        record.setBankName(member.getOpenAccountBank() + "-" + member.getOpenAccountAddress() + "-" + member.getOpenAccountSubbranch());
        save(record);

        return true;
    }

    @Override
    public Double getCashInAmount(String memberId) {
        Map<String, Double> result = memberCashInRecordsRepository.staticCashOnAmount(memberId);
        return result.get("totalAmount") == null ? 0d : result.get("totalAmount");
    }
}
