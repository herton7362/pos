package com.framework.module.member.web;

import com.framework.module.member.domain.MemberCashInRecords;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("提现记录")
@RestController
@RequestMapping("/api/memberCashInRecords")
public class MemberCashInRecordsController extends AbstractCrudController<MemberCashInRecords> {
}
