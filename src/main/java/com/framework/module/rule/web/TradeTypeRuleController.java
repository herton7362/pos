package com.framework.module.rule.web;

import com.framework.module.rule.domain.TradeTypeRule;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("#交易类型管理")
@RestController
@RequestMapping("/api/tradeTypeRule")
public class TradeTypeRuleController extends AbstractCrudController<TradeTypeRule> {
}
