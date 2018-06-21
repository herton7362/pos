package com.kratos.module.address.web;

import com.kratos.common.AbstractReadController;
import com.kratos.common.CrudService;
import com.kratos.module.address.domain.Address;
import com.kratos.module.address.service.AddressService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "游客地址接口，无权限过滤")
@RestController
@RequestMapping("/address")
public class GuestAddressController extends AbstractReadController<Address> {
    private final AddressService addressService;
    @Override
    protected CrudService<Address> getService() {
        return addressService;
    }

    @Autowired
    public GuestAddressController(AddressService addressService) {
        this.addressService = addressService;
    }
}
