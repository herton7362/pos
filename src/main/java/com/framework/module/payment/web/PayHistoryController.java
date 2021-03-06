package com.framework.module.payment.web;

import com.framework.module.payment.domain.PayHistory;
import com.framework.module.payment.domain.PayResult;
import com.framework.module.payment.service.PayHistoryService;
import com.kratos.common.AbstractCrudController;
import com.kratos.exceptions.BusinessException;
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

import java.util.Map;

@Api(value = "代付转账接口")
@RestController
@RequestMapping("/api/payHistory")
public class PayHistoryController extends AbstractCrudController<PayHistory> {

    private final PayHistoryService payHistoryService;

    public PayHistoryController(PayHistoryService payHistoryService) {
        this.payHistoryService = payHistoryService;
    }

    @ApiOperation(value = "转账汇款代付")
    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "登录返回token", name = "access_token", dataType = "String", paramType = "query")})
    public ResponseEntity<Map<String, Object>> pay(@RequestParam() String cashInId) throws Exception {
        PayResult payResult = payHistoryService.pay(cashInId);
        if (!"000000".equals(payResult.getResultCode())) {
            throw new BusinessException("第三方异常" + payResult.getResultCode() + "-" + payResult.getResultDes());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "转账汇款代付")
    @RequestMapping(value = "/getPayInfo", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "登录返回token", name = "access_token", dataType = "String", paramType = "query")})
    public ResponseEntity<PayHistory> getPayInfo(@RequestParam() String paymentId) throws Exception {
        return new ResponseEntity<>(payHistoryService.getPayInfo(paymentId), HttpStatus.OK);
    }
}
