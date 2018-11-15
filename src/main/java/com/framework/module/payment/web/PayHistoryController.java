package com.framework.module.payment.web;

import com.framework.module.payment.domain.PayHistory;
import com.framework.module.payment.service.PayHistoryService;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
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
@RequestMapping("/payHistory")
public class PayHistoryController extends AbstractCrudController<PayHistory> {

    private final PayHistoryService payHistoryService;

    public PayHistoryController(PayHistoryService payHistoryService) {
        this.payHistoryService = payHistoryService;
    }

    @ApiOperation(value = "转账汇款代付")
    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> pay(@RequestParam() String cashInId) {
        payHistoryService.pay(cashInId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
