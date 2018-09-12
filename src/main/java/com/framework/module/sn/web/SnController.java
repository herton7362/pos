package com.framework.module.sn.web;

import java.util.List;

import com.kratos.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.framework.module.sn.domain.SnInfo;
import com.framework.module.sn.service.SnInfoService;
import com.kratos.common.AbstractCrudController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
     * 管理员划拨未分配的SN
     */
    @ApiOperation(value = "管理员划拨未分配的SN")
    @RequestMapping(value = "/transSnByAdmin", method = RequestMethod.GET)
    public ResponseEntity<?> transSnByAdmin(@RequestParam String sns, @RequestParam String memberId) throws Exception {
        snInfoService.transSnByAdmin(sns, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 会员划拨给儿子
     */
    @ApiOperation(value = "会员划拨给儿子")
    @RequestMapping(value = "/transSnByMember", method = RequestMethod.GET)
    public ResponseEntity<?> transSnByMember(@RequestParam String sns, @RequestParam String memberId, @RequestParam String currentMemberId) throws Exception {
        snInfoService.transSnByMember(sns, memberId, currentMemberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 查询会员下可分配的SN信息
     */
    @ApiOperation(value = "查询会员下可分配的SN信息")
    @RequestMapping(value = "/getAvailableSn", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getAvailableSn() {
        return new ResponseEntity<>(snInfoService.getAvailableSn(), HttpStatus.OK);
    }

    @ApiOperation(value = "查询所有SN信息，有筛选功能")
    @RequestMapping(value = "/getAllSnInfo", method = RequestMethod.GET)
    public ResponseEntity<List<SnInfo>> getAllSnInfo(@RequestParam(required = false) String startSn, @RequestParam(required = false) String endSn, @RequestParam(required = false) SnInfo.Status status, @RequestParam(required = false) SnInfo.BindStatus bindStatus, @RequestParam() Integer pageSize, @RequestParam() Integer pageNum, @RequestParam(required = false) String memberId) throws Exception {
        return new ResponseEntity<>(snInfoService.getAllSnInfo(startSn, endSn, status, bindStatus, pageSize, pageNum, memberId), HttpStatus.OK);
    }

    @ApiOperation(value = "查询待分配列表")
    @RequestMapping(value = "/getUnDistributionList", method = RequestMethod.GET)
    public ResponseEntity<List<SnInfo>> getUnDistributionList(@RequestParam String memberId) {
        List<SnInfo> result = snInfoService.getUnDistributionList(memberId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
