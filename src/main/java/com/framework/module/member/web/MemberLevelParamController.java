package com.framework.module.member.web;

import com.framework.module.member.domain.MemberLevelParam;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("收益设置")
@RestController
@RequestMapping("/api/memberLevelParam")
public class MemberLevelParamController extends AbstractCrudController<MemberLevelParam> {
}
