package com.framework.module.member.web;

import com.framework.module.auth.MemberThread;
import com.framework.module.member.domain.*;
import com.framework.module.member.service.MemberLevelService;
import com.framework.module.member.service.MemberService;
import com.framework.module.orderform.service.OrderFormService;
import com.framework.module.shop.domain.Shop;
import com.framework.module.shop.domain.ShopRepository;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.common.PageParam;
import com.kratos.common.PageResult;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.AdminThread;
import com.kratos.module.auth.UserThread;
import com.kratos.module.auth.domain.Admin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Api(value = "会员管理")
@RestController
@RequestMapping("/api/member")
public class MemberController extends AbstractCrudController<Member> {
    private final MemberService memberService;
    private final MemberLevelService memberLevelService;
    private final MemberProfitRecordsRepository memberProfitRecordsRepository;
    private final RealIdentityAuditRepository realIdentityAuditRepository;
    private final OrderFormService orderFormService;
    private final MemberRepository memberRepository;
    private final ShopRepository shopRepository;

    @Override
    protected CrudService<Member> getService() {
        return memberService;
    }

    /**
     * 根据会员卡获取会员
     */
    @ApiOperation(value = "根据会员卡获取会员")
    @RequestMapping(value = "/cardNo/{cardNo}", method = RequestMethod.GET)
    public ResponseEntity<Member> getOneByCardNo(@PathVariable String cardNo) throws Exception {
        Member member = memberService.findOneByCardNo(cardNo);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    /**
     * 快速积分
     */
    @ApiOperation(value = "快速积分")
    @RequestMapping(value = "/fastIncreasePoint", method = RequestMethod.POST)
    public ResponseEntity<?> fastIncreasePoint(@RequestBody FastIncreasePointParam param) throws Exception {
        memberService.fastIncreasePoint(param.getMemberId(), param.getPoint());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 储值扣费
     */
    @ApiOperation(value = "储值扣费")
    @RequestMapping(value = "/deductBalance", method = RequestMethod.POST)
    public ResponseEntity<?> deductBalance(@RequestBody DeductBalanceParam param) throws Exception {
        memberService.deductBalance(param.getMemberId(), param.getAmount());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 获取会员优惠卷
     */
    @ApiOperation(value = "获取会员优惠卷")
    @RequestMapping(value = "/coupon/{memberId}", method = RequestMethod.GET)
    public ResponseEntity<List<CouponResult>> getCoupons(@PathVariable String memberId) throws Exception {
        Member member = memberService.findOne(memberId);
        final List<CouponResult> coupons = new ArrayList<>();
        if (member.getCoupons() != null) {
            member.getCoupons().forEach(memberCoupon -> {
                CouponResult couponResult = new CouponResult();
                BeanUtils.copyProperties(memberCoupon.getCoupon(), couponResult);
                coupons.add(couponResult);
            });
        }
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    /**
     * 查询总数
     */
    @ApiOperation(value = "查询总数")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public ResponseEntity<Long> count() {
        return new ResponseEntity<>(memberService.count(), HttpStatus.OK);
    }

    /**
     * 查询盟友
     *
     * @param sortType 排序类型
     * @return 响应
     * @throws Exception 异常
     */
    @ApiOperation(value = "查询盟友总数")
    @RequestMapping(value = "/myAllies/{sortType}", method = RequestMethod.GET)
    public ResponseEntity<List<Member>> myAllies(@PathVariable Integer sortType) throws Exception {
        String memberId = UserThread.getInstance().get().getId();
        AllyMemberInfos allyMembers = memberService.getAlliesInfosByMemberId(memberId, new Date().getTime());
        List<Member> result = new ArrayList<>();
        result.addAll(allyMembers.getSonList());
        result.addAll(allyMembers.getGrandSonList());
        if (CollectionUtils.isEmpty(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        if (sortType == null) {
            sortType = 1;
        }
        for (Member m : result) {
            m.setSortType(sortType);
            Map<String, Double> totalProfit = memberProfitRecordsRepository.staticTotalProfit(m.getId());
            m.setBalance(totalProfit.get("totalProfit") == null ? 0d : totalProfit.get("totalProfit"));
            m.setAllyNumber(memberService.getAlliesByMemberId(m.getId()).getTotalNum());
            RealIdentityAudit realIdentityAudit = realIdentityAuditRepository.findByMemberId(m.getId());
            if (realIdentityAudit != null && RealIdentityAudit.Status.PASS.equals(realIdentityAudit.getStatus())) {
                m.setRealIdentity(1);
            } else {
                m.setRealIdentity(0);
            }
            m.setBuyEquipmentNum(String.valueOf(orderFormService.getPayedOrderItemCounts(m.getId())));
        }
        Collections.sort(result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "查询盟友总数")
    @RequestMapping(value = "/myAlliesId", method = RequestMethod.GET)
    public ResponseEntity<List<String>> myAlliesId() throws Exception {
        String memberId = UserThread.getInstance().get().getId();
        AllyMemberInfos allyMembers = memberService.getAlliesInfosByMemberId(memberId, new Date().getTime());
        List<Member> result = new ArrayList<>();
        result.addAll(allyMembers.getSonList());
        result.addAll(allyMembers.getGrandSonList());

        List<String> result1 = new ArrayList<>();
        for (Member member : result) {
            result1.add(member.getId());
        }

        return new ResponseEntity<>(result1, HttpStatus.OK);
    }

    /**
     * 查询总数
     */
    @ApiOperation(value = "查询总数")
    @RequestMapping(value = "/level/{memberId}", method = RequestMethod.GET)
    public ResponseEntity<MemberLevel> count(@PathVariable String memberId) throws Exception {
        return new ResponseEntity<>(memberLevelService.getMemberMemberLevel(memberId), HttpStatus.OK);
    }

    /**
     * 获取盟友
     */
    @ApiOperation(value = "获取盟友", notes = "可以传查询条件例：name=张三，查询条件使用or分割")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "currentPage", value = "当前页数", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页页数", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序属性，多个用逗号隔开", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方向，多个用逗号隔开", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/allies", method = RequestMethod.GET)
    public ResponseEntity<?> searchAlliesPagedList(@ModelAttribute PageParam pageParam, HttpServletRequest request) throws Exception {
        Map<String, String[]> param = new HashMap<>(request.getParameterMap());
        param.put("fatherId", new String[]{MemberThread.getInstance().get().getId()});
        if (pageParam.isPageAble()) {
            PageResult<Member> page = crudService.findAll(pageParam.getPageRequest(), param);
            return new ResponseEntity<>(page, HttpStatus.OK);
        }
        List<Member> list = crudService.findAll(param);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * @return 查询所有子节点
     */
    @ApiOperation(value = "查询盟友总数")
    @RequestMapping(value = "/queryAllies", method = RequestMethod.GET)
    public ResponseEntity<List<Member>> queryAllies() throws Exception {
        String memberId = AdminThread.getInstance().get().getMemberId();
        AllyMemberInfos allyMembers = memberService.getAlliesInfosByMemberId(memberId, new Date().getTime());
        List<Member> result = new ArrayList<>();

        if (!CollectionUtils.isEmpty(allyMembers.getSonList())) {
            for (Member m : allyMembers.getSonList()) {
                RealIdentityAudit realIdentityAudit = realIdentityAuditRepository.findByMemberId(m.getId());
                if (realIdentityAudit != null && RealIdentityAudit.Status.PASS.equals(realIdentityAudit.getStatus())) {
                    result.add(m);
                }
            }
        }
        if (!CollectionUtils.isEmpty(allyMembers.getGrandSonList())) {
            for (Member m : allyMembers.getGrandSonList()) {
                RealIdentityAudit realIdentityAudit = realIdentityAuditRepository.findByMemberId(m.getId());
                if (realIdentityAudit != null && RealIdentityAudit.Status.PASS.equals(realIdentityAudit.getStatus())) {
                    result.add(m);
                }
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 查询盟友
     *
     * @return 响应
     * @throws Exception 异常
     */
    @ApiOperation(value = "查询盟友总数")
    @RequestMapping(value = "/myAlliesV2", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "登录返回token", name = "access_token", dataType = "String", paramType = "query")})
    @ResponseBody
    public ResponseEntity<List<Member>> myAlliesV2() throws Exception {
        String memberId = UserThread.getInstance().get().getId();
        List<Member> currentSons = memberRepository.findMemberInfosByFatherId(memberId, new Date().getTime());
        if (CollectionUtils.isEmpty(currentSons)) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        if (!CollectionUtils.isEmpty(currentSons)) {
            for (Member m : currentSons) {
                RealIdentityAudit realIdentityAudit = realIdentityAuditRepository.findByMemberId(m.getId());
                if (realIdentityAudit != null && RealIdentityAudit.Status.PASS.equals(realIdentityAudit.getStatus())) {
                    m.setRealIdentity(1);
                } else {
                    m.setRealIdentity(0);
                }
                List<Member> sonList = new ArrayList<>();
                AllyMemberInfos allyMembers = memberService.getAlliesInfosByMemberId(m.getId(), new Date().getTime());
                if (allyMembers != null) {
                    sonList.addAll(allyMembers.getSonList());
                    sonList.addAll(allyMembers.getGrandSonList());
                }
                if (!CollectionUtils.isEmpty(sonList)) {
                    m.setPartnerNum(sonList.size());
                    int activeNum = 0;
                    for (Member t : sonList) {
                        if (Member.Status.ACTIVE.equals(t.getStatus())) {
                            activeNum++;
                        }
                    }
                    m.setActivePartnerNum(activeNum);
                }
            }
        }
        return new ResponseEntity<>(currentSons, HttpStatus.OK);
    }

    /**
     * 查询盟友
     *
     * @return 响应
     * @throws Exception 异常
     */
    @ApiOperation(value = "查询盟友总数")
    @RequestMapping(value = "/searchAllies", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "登录返回token", name = "access_token", dataType = "String", paramType = "query")})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchAllies(@RequestParam String mobile, @RequestParam Long startTime, @RequestParam Long endTime) throws Exception {
        Member searchMember = memberRepository.findOneByLoginName(mobile);
        if (searchMember == null) {
            throw new BusinessException("无该用户,用户手机号" + mobile);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("partnerName", searchMember.getName());
        List<String> sonList = new ArrayList<>();
        sonList.add(searchMember.getId());
        AllyMembers allyMembers = memberService.getAlliesByMemberId(searchMember.getId(), new Date().getTime());
        if (allyMembers != null) {
            sonList.addAll(allyMembers.getSonList());
            sonList.addAll(allyMembers.getGrandSonList());
        }
        if (CollectionUtils.isEmpty(sonList)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        double totalTransactionAmount = 0d;
        int shopSize = 0;
        for (String son : sonList) {
            Map<String, Double> resultMap = memberProfitRecordsRepository.staticProfitsByMonthNew(son, startTime, endTime);
            double transactionAmount = resultMap.get("totalTransactionAmount") == null ? 0d : resultMap.get("totalTransactionAmount");
            totalTransactionAmount += transactionAmount;

            List<Shop> shops = shopRepository.findAllByMemberId(son, 0, endTime);
            shopSize += (shops == null ? 0 : shops.size());
        }
        totalTransactionAmount = new BigDecimal(totalTransactionAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        result.put("totalTransactionAmount", totalTransactionAmount);
        result.put("shopNum", shopSize);
        result.put("partnerNum", sonList.size());

        result.putAll(memberService.getActiveSonNum());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "关系树查询", notes = "关系树查询")
    @RequestMapping(value = "/searchForTree", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity<List<Member>> searchForTree(@RequestParam(required = false) String memberId) throws Exception {
        if (UserThread.getInstance().get() instanceof Admin) {
            memberId = AdminThread.getInstance().get().getMemberId();
        }
        if (StringUtils.isEmpty(memberId)) {
            Map<String, String[]> param = new HashMap<>();
            return new ResponseEntity<>(getService().findAll(param), HttpStatus.OK);
        }
        AllyMemberInfos allyMemberInfos = memberService.getAlliesInfosByMemberId(memberId, new Date().getTime());
        List<Member> result = new ArrayList<>();
        result.addAll(allyMemberInfos.getGrandSonList());
        result.addAll(allyMemberInfos.getSonList());
        do {
            Member one = memberService.findOne(memberId);
            result.add(one);
            memberId = one.getFatherId();
        } while (!StringUtils.isEmpty(memberId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Autowired
    public MemberController(
            MemberService memberService,
            MemberLevelService memberLevelService, MemberProfitRecordsRepository memberProfitRecordsRepository, RealIdentityAuditRepository realIdentityAuditRepository, OrderFormService orderFormService, MemberRepository memberRepository, ShopRepository shopRepository) {
        this.memberService = memberService;
        this.memberLevelService = memberLevelService;
        this.memberProfitRecordsRepository = memberProfitRecordsRepository;
        this.realIdentityAuditRepository = realIdentityAuditRepository;
        this.orderFormService = orderFormService;
        this.memberRepository = memberRepository;
        this.shopRepository = shopRepository;
    }
}
