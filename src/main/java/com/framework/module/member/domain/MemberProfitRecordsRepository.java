package com.framework.module.member.domain;

import com.kratos.common.PageRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.Map;

public interface MemberProfitRecordsRepository extends PageRepository<MemberProfitRecords> {

        @Query("SELECT sum(CASE p.profitType WHEN 1 THEN p.profit ELSE 0 END) AS activeAward, sum(CASE p.profitType WHEN 2 THEN p.profit ELSE 0 END) AS directlyAward, sum(CASE p.profitType WHEN 3 THEN p.profit ELSE 0 END) AS managerAward, sum(CASE p.profitType WHEN 4 THEN p.profit ELSE 0 END) AS teamBuildAward, sum(p.profit) AS totalProfit, sum(p.transactionAmount) AS totalTransactionAmount FROM MemberProfitRecords p where p.memberId=?1 AND p.transactionDate>=?2 AND p.transactionDate<=?3")
        Map<String, Double> staticProfitsByMonth(String memberId, long start, long end);

        @Query("SELECT sum(CASE p.transactionType WHEN 1 THEN p.transactionAmount ELSE 0 END) AS transaction1, sum(CASE p.transactionType WHEN 2 THEN p.transactionAmount ELSE 0 END) AS transaction2, sum(CASE p.transactionType WHEN 3 THEN p.transactionAmount ELSE 0 END) AS transaction3, sum(CASE p.transactionType WHEN 1 THEN p.profit ELSE 0 END) AS profit1, sum(CASE p.transactionType WHEN 2 THEN p.profit ELSE 0 END) AS profit2, sum(CASE p.transactionType WHEN 3 THEN p.profit ELSE 0 END) AS profit3 FROM MemberProfitRecords p where p.memberId=?1 AND p.transactionDate>=?2 AND p.transactionDate<=?3 AND p.profitType=2")
        Map<String, Double> staticDirectAwardByMonth(String memberId, long start, long end);

        @Query("SELECT sum(p.profit) AS totalProfit FROM MemberProfitRecords p where p.memberId=?1")
        Map<String, Double> staticTotalProfit(String memberId);

        @Query("SELECT sum(CASE p.profitType WHEN 2 THEN p.profit ELSE 0 END) AS directlyAward FROM MemberProfitRecords p where p.sn=?1 AND p.transactionDate>=?2 AND p.transactionDate<=?3")
        Map<String, Double> staticProfitsBySnMonth(String sn, long start, long end);
}
