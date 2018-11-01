package com.framework.module.version.web;

import com.framework.module.sn.domain.SnInfo;
import com.framework.module.version.domain.AndroidVersion;
import com.framework.module.version.service.AndroidVersionService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(value = "Android版本管理")
@RestController
@RequestMapping("/androidVersion")
public class AndroidVersionController extends AbstractCrudController<AndroidVersion> {

    private final AndroidVersionService androidVersionService;

    public AndroidVersionController(AndroidVersionService androidVersionService) {
        this.androidVersionService = androidVersionService;
    }

    @ApiOperation(value = "获得版本更新")
    @RequestMapping(value = "/getNewestVersion", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getNewestVersion(@RequestParam() Integer versionCode) {
        AndroidVersion androidVersion = androidVersionService.getNewestVersion(versionCode);
        Map<String, Object> result = new HashMap<>();
        result.put("data", androidVersion);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
