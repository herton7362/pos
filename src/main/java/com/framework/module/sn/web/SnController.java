package com.framework.module.sn.web;

import com.framework.module.member.domain.AchievementDetail;
import com.framework.module.sn.domain.SnInfo;
import com.framework.module.sn.service.SnInfoService;
import com.kratos.common.AbstractCrudController;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.UserThread;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    /**
     * 审核收益信息
     */
    @ApiOperation(value = "管理员划拨未分配的SN")
    @RequestMapping(value = "/transSnByAdmin", method = RequestMethod.GET)
    public ResponseEntity<?> transSnByAdmin(@RequestParam String sns, String memberId) throws Exception {
        snInfoService.transSnByAdmin(sns, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
