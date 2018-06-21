package com.kratos.module.dictionary.web;

import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.module.dictionary.domain.DictionaryCategory;
import com.kratos.module.dictionary.service.DictionaryCategoryService;
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

@Api(value = "字典类别管理")
@RestController
@RequestMapping("/api/dictionaryCategory")
public class DictionaryCategoryController extends AbstractCrudController<DictionaryCategory> {
    private final DictionaryCategoryService dictionaryCategoryService;
    @Override
    protected CrudService<DictionaryCategory> getService() {
        return dictionaryCategoryService;
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<DictionaryCategory> save(@RequestBody DictionaryCategory productCategory) throws Exception {
        if(productCategory.getParent() != null && StringUtils.isNotBlank(productCategory.getParent().getId())) {
            productCategory.setParent(dictionaryCategoryService.findOne(productCategory.getParent().getId()));
        } else {
            productCategory.setParent(null);
        }
        productCategory = dictionaryCategoryService.save(productCategory);
        return new ResponseEntity<>(productCategory, HttpStatus.OK);
    }

    @Autowired
    public DictionaryCategoryController(DictionaryCategoryService dictionaryCategoryService) {
        this.dictionaryCategoryService = dictionaryCategoryService;
    }
}
