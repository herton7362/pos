package com.framework.module.rule.web;

import com.framework.module.rule.domain.GroupBuildPurchaseRule;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("#团建最低采购数设置")
@RestController
@RequestMapping("/api/groupBuildPurchaseRule")
public class GroupBuildPurchaseRuleController extends AbstractCrudController<GroupBuildPurchaseRule> {
}
