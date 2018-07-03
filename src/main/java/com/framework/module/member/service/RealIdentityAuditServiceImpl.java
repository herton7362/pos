package com.framework.module.member.service;

import com.framework.module.member.domain.Member;
import com.framework.module.member.domain.RealIdentityAudit;
import com.kratos.common.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class RealIdentityAuditServiceImpl extends AbstractCrudService<RealIdentityAudit> implements RealIdentityAuditService {
    private final MemberService memberService;
    @Override
    public RealIdentityAudit save(RealIdentityAudit realIdentityAudit) throws Exception {
        if(realIdentityAudit.getStatus() == RealIdentityAudit.Status.PASS) {
            Member member= memberService.findOne(realIdentityAudit.getMemberId());
            member.setName(realIdentityAudit.getName());
            memberService.save(member);
        }
        return super.save(realIdentityAudit);
    }


    @Autowired
    public RealIdentityAuditServiceImpl(MemberService memberService) {
        this.memberService = memberService;
    }
}
