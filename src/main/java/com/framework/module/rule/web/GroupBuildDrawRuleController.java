package com.framework.module.rule.web;

import com.framework.module.rule.domain.GroupBuildDrawRule;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("#团建拉人奖励规则")
@RestController
@RequestMapping("/api/groupBuildDrawRule")
public class GroupBuildDrawRuleController extends AbstractCrudController<GroupBuildDrawRule> {
}
