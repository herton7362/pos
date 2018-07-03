package com.framework.module.member.service;

import com.framework.module.member.domain.*;
import com.kratos.common.CrudService;
import com.kratos.exceptions.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

/**
 * 提现业务类
 */
public interface MemberCashInRecordsService extends CrudService<MemberCashInRecords>  {

    boolean cashIn(String memberId, Double cashOnAmount) throws Exception;
}
