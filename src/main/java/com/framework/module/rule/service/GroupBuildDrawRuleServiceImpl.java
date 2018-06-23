package com.framework.module.rule.service;

import com.framework.module.rule.domain.GroupBuildDrawRule;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class GroupBuildDrawRuleServiceImpl extends AbstractCrudService<GroupBuildDrawRule> implements GroupBuildDrawRuleService {
}
