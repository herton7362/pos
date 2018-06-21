package com.kratos.module.address.web;

import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.module.address.domain.Address;
import com.kratos.module.address.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "地址管理")
@RestController
@RequestMapping("/api/address")
public class AddressController extends AbstractCrudController<Address> {
    private final AddressService addressService;
    @Override
    protected CrudService<Address> getService() {
        return addressService;
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Address> save(@RequestBody Address productCategory) throws Exception {
        if(productCategory.getParent() != null && StringUtils.isNotBlank(productCategory.getParent().getId())) {
            productCategory.setParent(addressService.findOne(productCategory.getParent().getId()));
        } else {
            productCategory.setParent(null);
        }
        productCategory = addressService.save(productCategory);
        return new ResponseEntity<>(productCategory, HttpStatus.OK);
    }

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }
}
