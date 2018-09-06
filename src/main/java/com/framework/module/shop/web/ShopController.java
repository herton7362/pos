package com.framework.module.shop.web;

import com.framework.module.member.domain.MemberProfitRecordsRepository;
import com.framework.module.shop.domain.Shop;
import com.framework.module.shop.domain.ShopRepository;
import com.framework.module.shop.service.ShopExchangeRecordsService;
import com.framework.module.shop.service.ShopService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.utils.StringUtils;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.UserThread;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api("商户维护")
@RestController
@RequestMapping("/api/shop")
public class ShopController extends AbstractCrudController<Shop> {

    private final ShopRepository shopRepository;
    private final MemberProfitRecordsRepository memberProfitRecordsRepository;
    private final ShopExchangeRecordsService shopExchangeRecordsService;
    private final ShopService shopService;

    public ShopController(ShopRepository shopRepository, MemberProfitRecordsRepository memberProfitRecordsRepository, ShopExchangeRecordsService shopExchangeRecordsService, ShopService shopService) {
        this.shopRepository = shopRepository;
        this.memberProfitRecordsRepository = memberProfitRecordsRepository;
        this.shopExchangeRecordsService = shopExchangeRecordsService;
        this.shopService = shopService;
    }

    @ApiOperation(value = "获取商户信息")
    @RequestMapping(value = "/getMyShops", method = RequestMethod.GET)
    public ResponseEntity<List<Shop>> getMyShops(@RequestParam(required = false) String quickSearch) throws Exception {
        String memberId = UserThread.getInstance().get().getId();
        List<Shop> resultList = new ArrayList<>();
        Map<String, String[]> param = new HashMap<>();
        param.put("memberId", new String[]{memberId});
        param.put("status", new String[]{Shop.Status.ACTIVE.toString()});
        if (StringUtils.isNotBlank(quickSearch)) {
            param.put("quickSearch", new String[]{quickSearch});
        }
        List<Shop> list = shopService.findAll(param);
//        List<Shop> list = shopRepository.findAllByMemberId(memberId, 0, new Date().getTime());
        if (list != null) {
            for (Shop s : list) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -3);
                long startStaticTime = calendar.getTime().getTime();
                if (startStaticTime < s.getCreatedDate()) {
                    startStaticTime = s.getCreatedDate();
                }
                Map<String, Double> result = memberProfitRecordsRepository.staticTransactionAmountBySnMonth(s.getSn(), startStaticTime, new Date().getTime());
                if (result.get("directlyAward") == null || result.get("directlyAward") == 0) {
                    s.setActivity(0);
                    resultList.add(s);
                } else {
                    calendar.add(Calendar.MONTH, 2);
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
        int auditExchangeNum = 0;
        if (list != null) {
            for (Shop s : list) {
                if (Shop.Status.ACTIVE.equals(s.getStatus())) {
                    activeNum++;
                }
                if (s.getExchangePosMachine() != null) {
                    if (s.getExchangePosMachine() == 1) {
                        alreadyExchangeNum++;
                    } else if (s.getExchangePosMachine() == 2) {
                        auditExchangeNum++;
                    }
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("shopList", list);
        result.put("canExchangeNum", activeNum - alreadyExchangeNum - auditExchangeNum);
        result.put("alreadyExchangeNum", alreadyExchangeNum);
        result.put("auditExchangeNum", auditExchangeNum);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "兑换机器")
    @RequestMapping(value = "/exchangeMachine", method = RequestMethod.POST)
    public ResponseEntity<?> exchangeMachine(@RequestParam String shopIds) throws BusinessException {
        String memberId = UserThread.getInstance().get().getId();
        shopExchangeRecordsService.exchangeMachine(shopIds, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 审核兑换信息
     */
    @ApiOperation(value = "审核兑换信息")
    @RequestMapping(value = "/examineExchangeMachine/{exchangeId}/{examineResult}", method = RequestMethod.POST)
    public ResponseEntity<?> examineExchangeMachine(@PathVariable String exchangeId, @PathVariable boolean examineResult) throws BusinessException {
        shopExchangeRecordsService.examineExchangeMachine(exchangeId, examineResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
