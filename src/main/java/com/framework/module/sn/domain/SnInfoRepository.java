package com.framework.module.sn.domain;

import com.kratos.common.PageRepository;

public interface SnInfoRepository extends PageRepository<SnInfo> {

    Integer countAllBySn(String sn);

    SnInfo findFirstBySn(String sn);

}
