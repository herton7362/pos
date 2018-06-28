package com.framework.module.member.service;

import com.framework.module.member.domain.RealIdentityAudit;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class RealIdentityAuditServiceImpl extends AbstractCrudService<RealIdentityAudit> implements RealIdentityAuditService {
}
