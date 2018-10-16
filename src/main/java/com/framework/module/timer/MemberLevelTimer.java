package com.framework.module.timer;

import com.framework.module.member.service.MemberProfitRecordsService;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/7/2 13:24
 */
@Component
public class MemberLevelTimer {
    private final MemberProfitRecordsService memberProfitService;
    private final Logger logger = Logger.getLogger(MemberProfitRecordsService.class);

    public MemberLevelTimer(MemberProfitRecordsService memberProfitService) {
        this.memberProfitService = memberProfitService;
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    public void increaseMemberLevel() throws Exception {
        logger.info("Increase Member Level.");
        memberProfitService.membersIncreaseLevel();
    }

    //    @Scheduled(cron = "0 0 6 1 * ?")
    @Scheduled(cron = "0 16 17 16 * ?")
    public void addManagerProfit() throws Exception {
        logger.info("start add manager profit.");
        memberProfitService.addManagerProfit();
        logger.info("end add manager profit.");
    }
}
