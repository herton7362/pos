package com.framework.module.timer;

import com.framework.module.payment.domain.PayHistory;
import com.framework.module.payment.service.PayHistoryService;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/7/2 13:24
 */
@Component
public class PaymentTimer {
    private final Logger logger = Logger.getLogger(PaymentTimer.class);

    private final PayHistoryService payHistoryService;

    public PaymentTimer(PayHistoryService payHistoryService) {
        this.payHistoryService = payHistoryService;
    }

//    @Scheduled(cron = "0 0/10 * * * ?")
//    public void getPaymentInfo() throws Exception {
//        logger.info("get payment start.");
//        Map<String, String[]> param = new HashMap<>();
//        param.put("orderState", new String[]{"IN_PROCESS"});
//        List<PayHistory> payHistoryList = payHistoryService.findAll(param);
//        logger.info(payHistoryList.size() + " has in process.");
//        for (PayHistory p : payHistoryList) {
//            payHistoryService.getPayInfo(p.getId());
//        }
//        logger.info("end payment start.");
//    }
}
