package com.framework.module.shop.domain;

import com.framework.module.member.domain.Member;
import com.kratos.common.PageRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ShopRepository extends PageRepository<Shop> {
    Shop findOneBySn(String sn);

    @Query("select m from Shop m where m.memberId=?1 and m.createdDate>=?2 and m.createdDate<=?3")
    List<Shop> findAllByMemberId(String memberId, long start, long endDate);

    @Query("select count(m) from Shop m where m.memberId=?1 and m.createdDate>=?2 and m.createdDate<=?3")
    Integer countAllByMemberId(String memberId, long start, long endDate);

    @Query("SELECT sum(p.transactionAmount) AS totalTransactionAmount FROM Shop p where p.memberId=?1")
    Map<String, Double> staticTotalTransaction(String memberId);

    @Query("select m from Shop m where m.memberId=?1 and m.status='ACTIVE' and (m.exchangePosMachine=0 or m.exchangePosMachine IS null)")
    List<Shop> findAllUnExchangeByMemberId(String memberId);

    Integer countAllBySn(String sn);
}
