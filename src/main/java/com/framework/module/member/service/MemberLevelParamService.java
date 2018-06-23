package com.framework.module.member.service;

import com.framework.module.member.domain.MemberLevelParam;
import com.framework.module.member.domain.MemberProfitRecords;
import com.kratos.common.CrudService;

public interface MemberLevelParamService extends CrudService<MemberLevelParam> {

    MemberLevelParam getParamByLevel(String level) throws Exception;
}
