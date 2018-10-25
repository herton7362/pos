package com.framework.module.common;

import com.framework.module.member.domain.MemberProfitRecordsRepository;
import com.framework.module.member.domain.StaticStaticAchievement;
import com.framework.module.member.service.MemberProfitRecordsServiceImpl;
import com.framework.module.shop.domain.ShopRepository;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * <p>Description: </p>
 *
 * @Auther: 张庆贺
 * @Date: 2018/10/25 16:12
 */
public class StaticAchieveCallable implements Callable<StaticStaticAchievement> {
    private final Logger logger = Logger.getLogger(StaticAchieveCallable.class);
    private String memberId;
    private ShopRepository shopRepository;
    private MemberProfitRecordsRepository memberProfitRecordsRepository;
    private Long start;
    private Long end;

    public StaticAchieveCallable(Long start, long end, String memberId, ShopRepository shopRepository, MemberProfitRecordsRepository memberProfitRecordsRepository) {
        super();
        this.memberId = memberId;
        this.shopRepository = shopRepository;
        this.memberProfitRecordsRepository = memberProfitRecordsRepository;
        this.start = start;
        this.end = end;
    }

    @Override
    public StaticStaticAchievement call() {
        StaticStaticAchievement achievement = new StaticStaticAchievement();
        Integer shops = shopRepository.countAllByMemberId(memberId, start, end);
        achievement.setNewShopNum(shops == null ? 0 : shops);
        Map<String, Double> resultMap = memberProfitRecordsRepository.staticProfitsByMonthNew(memberId, start, end);
        achievement.setTransactionAmount(resultMap.get("totalTransactionAmount") == null ? 0d : resultMap.get("totalTransactionAmount"));
        return achievement;
    }
}
