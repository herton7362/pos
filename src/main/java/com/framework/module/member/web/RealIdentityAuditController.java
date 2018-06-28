package com.framework.module.member.web;

import com.framework.module.member.domain.RealIdentityAudit;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("实名认证")
@RestController
@RequestMapping("/api/realIdentityAudit")
public class RealIdentityAuditController extends AbstractCrudController<RealIdentityAudit> {
}
