package com.framework.module.shop.service;

import com.framework.module.shop.domain.ShopExchangeRecords;
import com.kratos.common.CrudService;
import com.kratos.common.PageResult;
import com.kratos.exceptions.BusinessException;

public interface ShopExchangeRecordsService extends CrudService<ShopExchangeRecords> {

    void exchangeMachine(String shopIds, String memberId, String shippingAddress) throws BusinessException;

    void examineExchangeMachine(String exchangeId, boolean examineResult) throws BusinessException;

    PageResult<ShopExchangeRecords> getAllExchangeRecords(String memberId, Integer currentPage, Integer pageSize, Long startTime, Long endTime);
}
