package com.kratos.module.dictionary.service;

import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import com.kratos.common.PageResult;
import com.kratos.module.dictionary.domain.DictionaryCategory;
import com.kratos.module.dictionary.domain.DictionaryCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@Transactional
public class DictionaryCategoryServiceImpl extends AbstractCrudService<DictionaryCategory> implements DictionaryCategoryService {
    private final DictionaryCategoryRepository dictionaryCategoryRepository;
    @Override
    protected PageRepository<DictionaryCategory> getRepository() {
        return dictionaryCategoryRepository;
    }

    @Override
    public List<DictionaryCategory> findAll(Map<String, String[]> param) throws Exception {
        return dictionaryCategoryRepository.findAll(this.getSpecificationForAllEntities(param));
    }

    @Override
    public PageResult<DictionaryCategory> findAll(PageRequest pageRequest, Map<String, String[]> param) throws Exception {
        return new PageResult<>(dictionaryCategoryRepository.findAll(this.getSpecificationForAllEntities(param), pageRequest));
    }

    @Autowired
    public DictionaryCategoryServiceImpl(DictionaryCategoryRepository dictionaryCategoryRepository) {
        this.dictionaryCategoryRepository = dictionaryCategoryRepository;
    }
}
