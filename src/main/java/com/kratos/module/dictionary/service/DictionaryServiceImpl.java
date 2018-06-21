package com.kratos.module.dictionary.service;

import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.common.PageResult;
import com.kratos.module.dictionary.domain.Dictionary;
import com.kratos.module.dictionary.domain.DictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@Transactional
public class DictionaryServiceImpl extends AbstractCrudService<Dictionary> implements DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    @Override
    protected PageRepository<Dictionary> getRepository() {
        return dictionaryRepository;
    }

    @Override
    public PageResult<Dictionary> findAll(PageRequest pageRequest, Map<String, String[]> param) throws Exception {
        return new PageResult<>(dictionaryRepository.findAll(this.getSpecificationForAllEntities(param), pageRequest));
    }

    @Override
    public List<Dictionary> findAll(Map<String, String[]> param) throws Exception {
        return dictionaryRepository.findAll(this.getSpecificationForAllEntities(param));
    }

    @Autowired
    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }
}
