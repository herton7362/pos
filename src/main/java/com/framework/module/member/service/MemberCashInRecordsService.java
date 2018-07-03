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

    /**
     * 提现功能
     * @param memberId
     * @param cashOnAmount
     * @return
     * @throws Exception
     */
    boolean cashIn(String memberId, Double cashOnAmount) throws Exception;

    /**
     *  查询已经提现的金额
     * @param memberId
     * @return
     */
    Double getCashInAmount(String memberId);

    /**
     * 查询所有提现记录
     * @param memberId 会员ID
     * @return  提现记录
     */
    List<MemberCashInRecords> getAllCashInRecords(String memberId);
}
