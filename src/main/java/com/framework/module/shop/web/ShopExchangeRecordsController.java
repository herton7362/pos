package com.framework.module.shop.web;

import com.framework.module.shop.domain.ShopExchangeRecords;
import com.framework.module.shop.service.ShopExchangeRecordsService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.PageResult;
import com.kratos.common.utils.StringUtils;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.AdminThread;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("商品兑换记录")
@RestController
@RequestMapping("/api/shopExchangeRecords")
public class ShopExchangeRecordsController extends AbstractCrudController<ShopExchangeRecords> {

    private final ShopExchangeRecordsService shopExchangeRecordsService;

    public ShopExchangeRecordsController(ShopExchangeRecordsService shopExchangeRecordsService) {
        this.shopExchangeRecordsService = shopExchangeRecordsService;
    }

    @ApiOperation(value = "查询所有子孙节点兑换信息", notes = "查询所有子孙节点兑换信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "currentPage", value = "当前页数", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页页数", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "起始时间", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "终止时间", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(value = "登录返回token", name = "access_token", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "getAllExchangeRecords", method = RequestMethod.GET)
    public ResponseEntity<PageResult<ShopExchangeRecords>> getAllExchangeRecords(@RequestParam Integer currentPage, @RequestParam Integer pageSize, @RequestParam(required = false) Long startTime, @RequestParam(required = false) Long endTime, @RequestParam(required = false) ShopExchangeRecords.Status status) throws Exception {
        String memberId = AdminThread.getInstance().get().getMemberId();
        if (StringUtils.isBlank(memberId)) {
            throw new BusinessException("未绑定会员信息");
        }
        return new ResponseEntity<>(shopExchangeRecordsService.getAllExchangeRecords(memberId, currentPage, pageSize, startTime, endTime, status), HttpStatus.OK);
    }
}
