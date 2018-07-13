package com.framework.module.shop.web;

import com.framework.module.shop.domain.ShopExchangeRecords;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("商品兑换记录")
@RestController
@RequestMapping("/api/shopExchangeRecords")
public class ShopExchangeRecordsController extends AbstractCrudController<ShopExchangeRecords> {
}
