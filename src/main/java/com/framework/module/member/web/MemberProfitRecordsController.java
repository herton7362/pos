package com.framework.module.member.web;

import com.framework.module.member.domain.MemberProfitRecords;
import com.kratos.common.AbstractCrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memberProfitRecords")
public class MemberProfitRecordsController extends AbstractCrudController<MemberProfitRecords> {
}
