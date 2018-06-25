package com.framework.module.rule.web;

import com.framework.module.rule.domain.ActiveRule;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("#收益规则管理")
@RestController
@RequestMapping("/api/activeRule")
public class ActiveRuleController extends AbstractCrudController<ActiveRule> {
}
