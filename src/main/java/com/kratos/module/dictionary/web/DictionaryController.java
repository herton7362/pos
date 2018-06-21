package com.kratos.module.dictionary.web;

import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.module.dictionary.domain.Dictionary;
import com.kratos.module.dictionary.domain.DictionaryCategory;
import com.kratos.module.dictionary.service.DictionaryCategoryService;
import com.kratos.module.dictionary.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "字典管理")
@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController extends AbstractCrudController<Dictionary> {
    private final DictionaryService dictionaryService;
    private final DictionaryCategoryService dictionaryCategoryService;
    @Override
    protected CrudService<Dictionary> getService() {
        return dictionaryService;
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Dictionary> save(@RequestBody Dictionary product) throws Exception {
        if(product.getDictionaryCategory() != null && StringUtils.isNotBlank(product.getDictionaryCategory().getId())) {
            product.setDictionaryCategory(dictionaryCategoryService.findOne(product.getDictionaryCategory().getId()));
        } else {
            product.setDictionaryCategory(null);
        }
        product = dictionaryService.save(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * 根据code获取字典
     */
    @ApiOperation(value="根据code获取字典")
    @RequestMapping(value= "/code/{code}", method = RequestMethod.GET)
    public ResponseEntity<List<Dictionary>> getByCode(@PathVariable String code) throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("code", new String[]{code});
        List<DictionaryCategory> dictionaryCategories  =  dictionaryCategoryService.findAll(params);
        List<Dictionary> dictionaries = new ArrayList<>();
        if(dictionaryCategories != null && !dictionaryCategories.isEmpty()) {
            params.clear();
            params.put("sort", new String[]{"sortNumber,updatedDate"});
            params.put("order", new String[]{"asc,desc"});
            params.put("dictionaryCategory.id", new String[]{dictionaryCategories.get(0).getId()});
            dictionaries = dictionaryService.findAll(params);
        }
        return new ResponseEntity<>(dictionaries, HttpStatus.OK);
    }

    @Autowired
    public DictionaryController(
            DictionaryService dictionaryService,
            DictionaryCategoryService dictionaryCategoryService
    ) {
        this.dictionaryService = dictionaryService;
        this.dictionaryCategoryService = dictionaryCategoryService;
    }
}
