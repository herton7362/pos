package com.framework.module.payment.domain;

import com.kratos.common.PageRepository;

public interface PayHistoryRepository extends PageRepository<PayHistory> {

    PayHistory findFirstByMchtOrderNo(String mchtOrderNo);

    PayHistory findFirstByCashInIdOrderByCreatedDateDesc(String cashInId);
}
