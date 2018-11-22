package com.framework.module.payment.web;

import com.framework.module.payment.service.PayHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "代付转账接口")
@RestController
@RequestMapping("/pay")
public class PayCallBackController {

    private final PayHistoryService payHistoryService;
    Logger logger = Logger.getLogger(PayCallBackController.class);

    public PayCallBackController(PayHistoryService payHistoryService) {
        this.payHistoryService = payHistoryService;
    }

    @ApiOperation(value = "转账汇款代付")
    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public ResponseEntity<?> pay(@RequestParam() String merchantOrderId,
                                 @RequestParam() String orderStatus,
                                 @RequestParam() String errorCode,
                                 @RequestParam() String errorMsg) throws Exception {
        String builder =
                "merchantOrderId" + merchantOrderId +
                        "orderStatus" + orderStatus +
                        "errorCode" + errorCode +
                        "errorMsg" + errorMsg;
        logger.info("callback result====" + builder);
        payHistoryService.callback(merchantOrderId, orderStatus, errorCode, errorMsg);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
