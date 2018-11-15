package com.framework.module.payment.service;

import com.framework.module.payment.domain.PayHistory;
import com.kratos.common.CrudService;

public interface PayHistoryService extends CrudService<PayHistory> {
    void pay(String cashInId) throws Exception;
}
