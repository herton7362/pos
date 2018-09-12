package com.framework.module.sn.domain;

import com.kratos.common.PageRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SnInfoRepository extends PageRepository<SnInfo> {

    Integer countAllBySn(String sn);

    SnInfo findFirstBySn(String sn);

    Integer countAllByMemberId(String sn);

    @Query("select m.sn from SnInfo m where m.memberId=?1 and m.shopId is null")
    List<String> getAvailableSnByMemberId(String memberId);

    List<SnInfo> findAllByMemberIdAndShopIdNull(String memberId);

}
