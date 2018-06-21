package com.kratos.module.address.service;

import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.common.PageResult;
import com.kratos.module.address.domain.Address;
import com.kratos.module.address.domain.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@Transactional
public class AddressServiceImpl extends AbstractCrudService<Address> implements AddressService {
    private final AddressRepository addressRepository;
    @Override
    protected PageRepository<Address> getRepository() {
        return addressRepository;
    }

    @Override
    public PageResult<Address> findAll(PageRequest pageRequest, Map<String, String[]> param) throws Exception {
        return new PageResult<>(addressRepository.findAll(this.getSpecificationForAllEntities(param), pageRequest));
    }

    @Override
    public List<Address> findAll(Map<String, String[]> param) throws Exception {
        return addressRepository.findAll(this.getSpecificationForAllEntities(param));
    }

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
}
