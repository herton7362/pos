package com.framework.module.member.domain;

import com.kratos.common.PageRepository;

import java.util.List;

public interface MemberProfitTmpRecordsRepository extends PageRepository<MemberProfitTmpRecords> {
    /**
     * 通过操作流水号查询
     * @param operateTransactionId 操作流水号
     * @return 入库数据
     */
    List<MemberProfitTmpRecords> findAllByOperateTransactionId(String operateTransactionId);
}
