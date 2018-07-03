package com.framework.module.member.domain;

import com.kratos.common.PageRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MemberCashInRecordsRepository extends PageRepository<MemberCashInRecords> {

    @Query("SELECT sum(p.cashAmount) AS totalAmount FROM MemberCashInRecords p where p.memberId=?1 AND (p.status='PENDING' OR p.status='PASS')")
    Map<String, Double> staticCashOnAmount(String memberId);
}
