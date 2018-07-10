package com.framework.module.member.service;

import com.framework.module.member.domain.MemberProfitTmpRecords;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class MemberProfitTmpRecordsServiceImpl extends AbstractCrudService<MemberProfitTmpRecords> implements MemberProfitTmpRecordsService {
}
