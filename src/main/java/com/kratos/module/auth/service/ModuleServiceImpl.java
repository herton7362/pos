package com.kratos.module.auth.service;

import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.common.PageResult;
import com.kratos.module.auth.domain.Module;
import com.kratos.module.auth.domain.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@Transactional
public class ModuleServiceImpl extends AbstractCrudService<Module> implements ModuleService {
    private final ModuleRepository moduleRepository;

    @Override
    public PageResult<Module> findAll(PageRequest pageRequest, Map<String, String[]> param) throws Exception {
        return new PageResult<>(moduleRepository.findAll(this.getSpecificationForAllEntities(param), pageRequest));
    }

    @Override
    public List<Module> findAll(Map<String, String[]> param) throws Exception {
        return moduleRepository.findAll(this.getSpecificationForAllEntities(param));
    }

    @Override
    protected PageRepository<Module> getRepository() {
        return moduleRepository;
    }

    @Autowired
    public ModuleServiceImpl(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }
}
