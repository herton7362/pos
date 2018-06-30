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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(new Date()) + "-01 00:00:00";

        String memberId = UserThread.getInstance().get().getId();
        List<Shop> list = shopRepository.findAllByMemberId(memberId, 0, new Date().getTime());
        if (list != null) {
            for (Shop s : list) {
                Map<String, Double> result = memberProfitRecordsRepository.staticProfitsBySnMonth(s.getSn(), sdf1.parse(dateStr).getTime(), new Date().getTime());
                if (result.get("directlyAward") != null && result.get("directlyAward") > 0) {
                    s.setActivity(1);
                } else {
                    s.setActivity(0);
                }
            }
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
