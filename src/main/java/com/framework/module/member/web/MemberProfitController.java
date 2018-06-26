package com.framework.module.member.web;

import com.framework.module.member.domain.Member;
import com.framework.module.member.service.MemberLevelService;
import com.framework.module.member.service.MemberProfitService;
import com.framework.module.member.service.MemberService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Api(value = "会员管理")
@RestController
@RequestMapping("/api/memberprofit")
public class MemberProfitController extends AbstractCrudController<Member> {
    private final MemberProfitService memberProfitService;

    @Override
    protected CrudService<Member> getService() {
        return memberProfitService;
    }

    @Autowired
    public MemberProfitController(
            MemberProfitService memberProfitService) {
        this.memberProfitService = memberProfitService;
    }

    /**
     * 获取会员优惠卷
     */
    @ApiOperation(value = "导入成员收益")
    @RequestMapping(value="/import/profit", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity<String> userProfit(@RequestParam("profitFile") MultipartFile profitFile) {
        String fileName = profitFile.getOriginalFilename();
        int insertSize = 0;
        try {
            insertSize = memberProfitService.batchImport(fileName, profitFile);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(insertSize+" data has been handled.", HttpStatus.OK);
    }

    /**
     * 获取当月收益详情
     */
    @ApiOperation(value = "获取当月收益详情")
    @RequestMapping(value = "/getProfitDetail/{memberId}", method = RequestMethod.GET)
    public ResponseEntity<List<CouponResult>> getProfitDetail(@PathVariable String memberId) throws Exception {
        Member member = memberProfitService.findOne(memberId);
        final List<CouponResult> coupons = new ArrayList<>();



        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }


}
