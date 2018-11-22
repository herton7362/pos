package com.framework.module.payment.service;

import com.framework.module.payment.domain.PayHistory;
import com.framework.module.payment.domain.PayResult;
import com.kratos.common.CrudService;

public interface PayHistoryService extends CrudService<PayHistory> {
    PayResult pay(String cashInId) throws Exception;

    PayHistory getPayInfo(String paymentId) throws Exception;

    void callback(String merchantOrderId, String orderStatus, String errorCode, String errorMsg) throws Exception;
}
