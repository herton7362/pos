package com.framework.module.member.web;

import com.framework.module.auth.MemberThread;
import com.framework.module.member.domain.*;
import com.framework.module.member.service.MemberLevelService;
import com.framework.module.member.service.MemberService;
import com.framework.module.orderform.service.OrderFormService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.common.PageParam;
import com.kratos.common.PageResult;
import com.kratos.module.auth.UserThread;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    public MemberController(
            MemberService memberService,
            MemberLevelService memberLevelService, MemberProfitRecordsRepository memberProfitRecordsRepository, RealIdentityAuditRepository realIdentityAuditRepository, OrderFormService orderFormService) {
        this.memberService = memberService;
        this.memberLevelService = memberLevelService;
        this.memberProfitRecordsRepository = memberProfitRecordsRepository;
        this.realIdentityAuditRepository = realIdentityAuditRepository;
        this.orderFormService = orderFormService;
    }
}
