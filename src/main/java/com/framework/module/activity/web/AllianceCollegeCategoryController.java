package com.framework.module.activity.web;

import com.framework.module.activity.domain.AllianceCollegeCategory;
import com.framework.module.product.domain.ProductCategory;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("联盟学院分类")
@RestController
@RequestMapping("/api/allianceCollegeCategory")
public class AllianceCollegeCategoryController extends AbstractCrudController<AllianceCollegeCategory> {
    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AllianceCollegeCategory> save(@RequestBody AllianceCollegeCategory allianceCollegeCategory) throws Exception {
        if(allianceCollegeCategory.getParent() != null && StringUtils.isNotBlank(allianceCollegeCategory.getParent().getId())) {
            allianceCollegeCategory.setParent(crudService.findOne(allianceCollegeCategory.getParent().getId()));
        } else {
            allianceCollegeCategory.setParent(null);
        }
        allianceCollegeCategory = crudService.save(allianceCollegeCategory);
        return new ResponseEntity<>(allianceCollegeCategory, HttpStatus.OK);
    }
}
