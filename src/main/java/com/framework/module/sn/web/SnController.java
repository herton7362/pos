package com.framework.module.sn.web;

import com.framework.module.sn.domain.SnInfo;
import com.framework.module.sn.service.SnInfoService;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "SN管理")
@RestController
@RequestMapping("/api/sn")
public class SnController extends AbstractCrudController<SnInfo> {
    private final SnInfoService snInfoService;

    public SnController(SnInfoService snInfoService) {
        this.snInfoService = snInfoService;
    }

    /**
     * 导入SN信息
     */
    @ApiOperation(value = "导入sn信息")
    @RequestMapping(value = "/importSn", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity<String> importSn(@RequestParam("snFile") MultipartFile snFile) {
        String fileName = snFile.getOriginalFilename();
        int insertSize;
        try {
            insertSize = snInfoService.batchImport(fileName, snFile);
        } catch (Exception e) {
            return new ResponseEntity<>("上传失败，原因是:" + e.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>("上传成功." + insertSize + "条数据被上传.", HttpStatus.OK);
    }
}
