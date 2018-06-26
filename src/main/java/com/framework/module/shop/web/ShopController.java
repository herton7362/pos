package com.framework.module.shop.web;

import com.framework.module.shop.domain.Shop;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("商户维护")
@RestController
@RequestMapping("/api/shop")
public class ShopController extends AbstractCrudController<Shop> {
}
