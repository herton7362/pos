package com.framework.module.shop.service;

import com.framework.module.shop.domain.Shop;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ShopServiceImpl extends AbstractCrudService<Shop> implements ShopService {
}
