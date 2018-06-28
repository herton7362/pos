package com.framework.module.member.web;

import com.framework.module.auth.MemberThread;
import com.framework.module.member.domain.Member;
import com.framework.module.member.domain.MemberLevel;
import com.framework.module.member.service.MemberLevelService;
import com.framework.module.member.service.MemberService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.common.PageParam;
import com.kratos.common.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "会员管理")
@RestController
@RequestMapping("/api/member")
public class MemberController extends AbstractCrudController<Member> {
    private final MemberService memberService;
    private final MemberLevelService memberLevelService;

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
    public ResponseEntity<Long> count() throws Exception {
        return new ResponseEntity<>(memberService.count(), HttpStatus.OK);
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
    @ApiOperation(value="获取盟友", notes = "可以传查询条件例：name=张三，查询条件使用or分割")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "currentPage", value = "当前页数", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页页数", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序属性，多个用逗号隔开", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order", value = "排序方向，多个用逗号隔开", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/allies", method = RequestMethod.GET)
    public ResponseEntity<?> searchPagedList(@ModelAttribute PageParam pageParam, HttpServletRequest request) throws Exception {
        Map<String, String[]> param = new HashMap<>(request.getParameterMap());
        param.put("fatherId", new String[]{MemberThread.getInstance().get().getId()});
        if(pageParam.isPageAble()) {
            PageResult<Member> page = crudService.findAll(pageParam.getPageRequest(), param);
            return new ResponseEntity<>(page, HttpStatus.OK);
        }
        List<Member> list = crudService.findAll(param);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Autowired
    public MemberController(
            MemberService memberService,
            MemberLevelService memberLevelService
    ) {
        this.memberService = memberService;
        this.memberLevelService = memberLevelService;
    }
}
