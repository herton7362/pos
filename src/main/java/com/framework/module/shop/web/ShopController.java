package com.framework.module.shop.web;

import com.framework.module.member.domain.MemberProfitRecordsRepository;
import com.framework.module.shop.domain.Shop;
import com.framework.module.shop.domain.ShopRepository;
import com.kratos.common.AbstractCrudController;
import com.kratos.module.auth.UserThread;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api("商户维护")
@RestController
@RequestMapping("/api/shop")
public class ShopController extends AbstractCrudController<Shop> {

    private final ShopRepository shopRepository;
    private final MemberProfitRecordsRepository memberProfitRecordsRepository;

    public ShopController(ShopRepository shopRepository, MemberProfitRecordsRepository memberProfitRecordsRepository) {
        this.shopRepository = shopRepository;
        this.memberProfitRecordsRepository = memberProfitRecordsRepository;
    }

    @ApiOperation(value = "获取商户信息")
    @RequestMapping(value = "/getMyShops", method = RequestMethod.GET)
    public ResponseEntity<List<Shop>> getMyShops() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);
        String memberId = UserThread.getInstance().get().getId();
        List<Shop> resultList = new ArrayList<>();
        List<Shop> list = shopRepository.findAllByMemberId(memberId, 0, new Date().getTime());
        if (list != null) {
            for (Shop s : list) {
                Map<String, Double> result = memberProfitRecordsRepository.staticTransactionAmountBySnMonth(s.getSn(), calendar.getTime().getTime(), new Date().getTime());
                if (result.get("directlyAward") == null || result.get("directlyAward") == 0) {
                    s.setActivity(0);
                    resultList.add(s);
                } else {
                    calendar.add(Calendar.MONTH, 1);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String source = sdf.format(calendar.getTime()) + "-01 00:00:00";
                    Date startDate = sdf1.parse(source);
                    long start = startDate.getTime();
                    calendar.setTime(startDate);
                    // 获取前一个月最后一天
                    calendar.add(Calendar.MONTH, 1);
                    calendar.set(Calendar.DAY_OF_MONTH, 0);
                    String lastDay = sdf1.format(calendar.getTime()) + " 23:59:59";
                    long end = sdf2.parse(lastDay).getTime();

                    Map<String, Double> lastMonth = memberProfitRecordsRepository.staticTransactionAmountBySnMonth(s.getSn(), start, end);
                    if (lastMonth.get("directlyAward") != null && lastMonth.get("directlyAward") > 200000) {
                        s.setActivity(1);
                        resultList.add(s);
                    }
                }
            }
        }
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @ApiOperation(value = "获取兑换机器商户列表信息")
    @RequestMapping(value = "/getExchangeMachineShopList", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getExchangeMachineShopList() {
        String memberId = UserThread.getInstance().get().getId();
        List<Shop> list = shopRepository.findAllByMemberId(memberId, 0, new Date().getTime());
        int activeNum = 0;
        int alreadyExchangeNum = 0;
        if (list != null) {
            for (Shop s : list) {
                if (Shop.Status.ACTIVE.equals(s.getStatus())) {
                    activeNum++;
                }
                if (s.getExchangePosMachine() != null && s.getExchangePosMachine() == 1) {
                    alreadyExchangeNum++;
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("shopList", list);
        result.put("canExchangeNum", activeNum - alreadyExchangeNum);
        result.put("alreadyExchangeNum", alreadyExchangeNum);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
