package com.framework.module.shop.service;

import com.framework.module.shop.domain.ShopExchangeRecords;
import com.kratos.common.CrudService;
import com.kratos.exceptions.BusinessException;

public interface ShopExchangeRecordsService extends CrudService<ShopExchangeRecords> {

    void exchangeMachine(String shopIds, String memberId, String shippingAddress) throws BusinessException;

    void examineExchangeMachine(String exchangeId, boolean examineResult) throws BusinessException;
}
