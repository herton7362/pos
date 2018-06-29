package com.framework.module.member.web;

import com.framework.module.member.domain.Achievement;
import com.framework.module.member.domain.AchievementDetail;
import com.framework.module.member.domain.MemberProfitRecords;
import com.framework.module.member.domain.ProfitMonthDetail;
import com.framework.module.member.service.MemberProfitRecordsService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.module.auth.UserThread;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(value = "会员管理")
@RestController
@RequestMapping("/api/memberprofit")
public class MemberProfitController extends AbstractCrudController<MemberProfitRecords> {
    private final MemberProfitRecordsService memberProfitService;

    @Override
    protected CrudService<MemberProfitRecords> getService() {
        return memberProfitService;
    }

    @Autowired
    public MemberProfitController(
            MemberProfitRecordsService memberProfitService) {
        this.memberProfitService = memberProfitService;
    }

    /**
     * 获取会员优惠卷
     */
    @ApiOperation(value = "导入成员收益")
    @RequestMapping(value = "/import/profit", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity<String> userProfit(@RequestParam("profitFile") MultipartFile profitFile) {
        String fileName = profitFile.getOriginalFilename();
        int insertSize;
        try {
            insertSize = memberProfitService.batchImport(fileName, profitFile);
        } catch (Exception e) {
            return new ResponseEntity<>("upload failed.reason:" + e.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>("upload success." + insertSize + " data has been handled.", HttpStatus.OK);
    }

    /**
     * 获取收益详情
     *
     * @param startMonth startMonth
     * @param size       size
     * @return 收益详情
     */
    @ApiOperation(value = "按照历史收益")
    @RequestMapping(value = "/getMonthProfit/{startMonth}/{size}", method = RequestMethod.GET)
    public ResponseEntity<List<ProfitMonthDetail>> getMonthProfit(@PathVariable String startMonth, @PathVariable Integer size) {
        List<ProfitMonthDetail> result = null;
        String memberId = UserThread.getInstance().get().getId();
        try {
            result = memberProfitService.getProfitByMonth(memberId, startMonth, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 获取当月收益详情
     *
     * @return 收益详情
     */
    @ApiOperation(value = "获取当月收益详情")
    @RequestMapping(value = "/getProfit", method = RequestMethod.GET)
    public ResponseEntity<List<ProfitMonthDetail>> getProfit() {
        String memberId = UserThread.getInstance().get().getId();
        List<ProfitMonthDetail> result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Calendar calendar = Calendar.getInstance();
            result = memberProfitService.getProfitByMonth(memberId, sdf.format(calendar.getTime()), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "按照月份获取历史业绩")
    @RequestMapping(value = "/getMonthAchievement/{startMonth}/{size}", method = RequestMethod.GET)
    public ResponseEntity<List<AchievementDetail>> getMonthAchievement(@PathVariable String startMonth, @PathVariable Integer size) {
        String memberId = UserThread.getInstance().get().getId();
        List<AchievementDetail> result = null;
        try {
            result = memberProfitService.getAchievementByMonth(memberId, startMonth, size);
        } catch (Exception e) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "按照天获取历史业绩")
    @RequestMapping(value = "/getDayAchievement/{startDate}/{size}", method = RequestMethod.GET)
    public ResponseEntity<List<AchievementDetail>> getDayAchievement(@PathVariable String startDate, @PathVariable Integer size) {
        String memberId = UserThread.getInstance().get().getId();
        List<AchievementDetail> result = new ArrayList<>();
        try {
            result = memberProfitService.getAchievementByDate(memberId, startDate, size);
        } catch (Exception e) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "获取业绩")
    @RequestMapping(value = "/getAchievement", method = RequestMethod.GET)
    public ResponseEntity<List<Achievement>> getAchievement() {
        String memberId = UserThread.getInstance().get().getId();
        List<Achievement> result = new ArrayList<>();
        try {
            result = result = memberProfitService.getAchievement(memberId);
        } catch (ParseException e) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "今日盟友登记商户")
    @RequestMapping(value = "/getAllyNewShopToday", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Integer>> getAllyNewShopToday() {
        Map<String, Integer> result = new HashMap<>();
        try {
            String memberId = UserThread.getInstance().get().getId();
            result.put("NewShopNum", memberProfitService.getAllyNewShopToday(memberId));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ParseException e) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "获取历史总收益")
    @RequestMapping(value = "/getTotalProfit", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Double>> getTotalProfit() {
        Map<String, Double> result = new HashMap<>();
        String memberId = UserThread.getInstance().get().getId();
        result.put("TotalProfit", memberProfitService.getTotalProfit(memberId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
