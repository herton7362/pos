package com.framework.module.member.domain;

import com.kratos.common.PageRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.Map;

public interface MemberProfitRecordsRepository extends PageRepository<MemberProfitRecords> {

        @Query("SELECT sum(CASE p.profitType WHEN 1 THEN p.profit ELSE 0 END) AS activeAward, sum(CASE p.profitType WHEN 2 THEN p.profit ELSE 0 END) AS directlyAward, sum(CASE p.profitType WHEN 3 THEN p.profit ELSE 0 END) AS managerAward, sum(CASE p.profitType WHEN 4 THEN p.profit ELSE 0 END) AS teamBuildAward, sum(p.profit) AS totalProfit, sum(p.transactionAmount) AS totalTransactionAmount FROM MemberProfitRecords p where p.memberId=?1 AND p.createdDate>=?2 AND p.createdDate<=?3")
        Map<String, Double> statisProfitsByMonth(String memberId, long start, long end);
}
