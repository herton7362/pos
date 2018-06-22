package com.framework.module.member.service;

import com.framework.module.member.domain.MemberCardType;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class MemberCardTypeServiceImpl extends AbstractCrudService<MemberCardType> implements MemberCardTypeService {
}
