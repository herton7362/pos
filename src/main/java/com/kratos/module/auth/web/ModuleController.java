package com.kratos.module.auth.web;

import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.module.auth.domain.Module;
import com.kratos.module.auth.service.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "模块管理")
@RestController
@RequestMapping("/api/module")
public class ModuleController extends AbstractCrudController<Module> {
    private final ModuleService moduleService;

    /**
     * 保存
     */
    @ApiOperation(value="保存")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Module> save(@RequestBody Module module) throws Exception {
        if(module.getParent() != null && StringUtils.isNotBlank(module.getParent().getId())) {
            module.setParent(moduleService.findOne(module.getParent().getId()));
        } else {
            module.setParent(null);
        }
        module = moduleService.save(module);
        return new ResponseEntity<>(module, HttpStatus.OK);
    }

    @Override
    protected CrudService<Module> getService() {
        return moduleService;
    }

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }
}
