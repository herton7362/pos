package com.framework.module.member.web;

import com.framework.module.member.domain.*;
import com.framework.module.member.service.MemberCashInRecordsService;
import com.framework.module.member.service.MemberProfitRecordsService;
import com.framework.module.member.service.MemberProfitTmpRecordsService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.UserThread;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Api(value = "会员收益管理")
@RestController
@RequestMapping("/api/memberprofit")
public class MemberProfitController extends AbstractCrudController<MemberProfitRecords> {
    private final MemberProfitRecordsService memberProfitService;
    private final MemberCashInRecordsService memberCashInRecordsService;
    private final MemberProfitTmpRecordsService memberProfitTmpRecordsService;

    @Override
    protected CrudService<MemberProfitRecords> getService() {
        return memberProfitService;
    }

    @Autowired
    public MemberProfitController(
            MemberProfitRecordsService memberProfitService,
            MemberCashInRecordsService memberCashInRecordsService,
            MemberProfitTmpRecordsService memberProfitTmpRecordsService
    ) {
        this.memberProfitService = memberProfitService;
        this.memberCashInRecordsService = memberCashInRecordsService;
        this.memberProfitTmpRecordsService = memberProfitTmpRecordsService;
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
            return new ResponseEntity<>("上传失败，原因是:" + e.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>("上传成功." + insertSize + "条数据被上传.", HttpStatus.OK);
    }

    /**
     * 审核收益信息
     */
    @ApiOperation(value = "审核收益信息")
    @RequestMapping(value = "/examineImportProfit/{operateTransactionId}/{examineResult}", method = RequestMethod.POST)
    public ResponseEntity<?> examineImportProfit(@PathVariable String operateTransactionId, @PathVariable boolean examineResult) throws Exception {
        memberProfitService.examineImportProfit(operateTransactionId, examineResult);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<List<ProfitMonthDetail>> getMonthProfit(@PathVariable String startMonth, @PathVariable Integer size) throws Exception {
        String memberId = UserThread.getInstance().get().getId();
        List<ProfitMonthDetail> result = memberProfitService.getProfitByMonth(memberId, startMonth, size);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 获取当月收益详情
     *
     * @return 收益详情
     */
    @ApiOperation(value = "获取当月收益详情")
    @RequestMapping(value = "/getProfit", method = RequestMethod.GET)
    public ResponseEntity<List<ProfitMonthDetail>> getProfit() throws Exception {
        String memberId = UserThread.getInstance().get().getId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        List<ProfitMonthDetail> result = memberProfitService.getProfitByMonth(memberId, sdf.format(calendar.getTime()), 1);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "按照月份获取历史业绩")
    @RequestMapping(value = "/getMonthAchievement/{startMonth}/{size}", method = RequestMethod.GET)
    public ResponseEntity<List<AchievementDetail>> getMonthAchievement(@PathVariable String startMonth, @PathVariable Integer size) {
        String memberId = UserThread.getInstance().get().getId();
        List<AchievementDetail> result = new ArrayList<>();
        try {
            result = memberProfitService.getAchievementByMonth(memberId, startMonth, size);
        } catch (Exception e) {
            return new ResponseEntity<>(result, BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "按照天获取历史业绩")
    @RequestMapping(value = "/getDayAchievement/{startDate}/{size}", method = RequestMethod.GET)
    public ResponseEntity<List<AchievementDetail>> getDayAchievement(@PathVariable String startDate, @PathVariable Integer size) throws Exception {
        String memberId = UserThread.getInstance().get().getId();
        List<AchievementDetail> result = memberProfitService.getAchievementByDate(memberId, startDate, size);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "获取业绩")
    @RequestMapping(value = "/getAchievement", method = RequestMethod.GET)
    public ResponseEntity<List<Achievement>> getAchievement() {
        String memberId = UserThread.getInstance().get().getId();
        List<Achievement> result = new ArrayList<>();
        try {
            result = memberProfitService.getAchievement(memberId);
        } catch (ParseException e) {
            return new ResponseEntity<>(result, BAD_REQUEST);
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

    @ApiOperation(value = "查询提现金额")
    @RequestMapping(value = "/getAllowCashInAmount", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Double>> getAllowCashInAmount() throws Exception {
        Map<String, Double> result = new HashMap<>();
        String memberId = UserThread.getInstance().get().getId();
        result.put("CashInAmount", memberProfitService.cashOnAmount(memberId));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "用户提现")
    @RequestMapping(value = "/userCashIn", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ApiImplicitParams({@ApiImplicitParam(name = "amount", value = "提现金额", dataType = "double", paramType = "query", required = true)})
    public ResponseEntity<Map<String, String>> userCashIn(@RequestParam(value = "amount") double amount) throws Exception {
        String memberId = UserThread.getInstance().get().getId();
        double allowCashInAmout = memberProfitService.cashOnAmount(memberId);
        if (amount > allowCashInAmout) {
            throw new BusinessException("提现金额超出允许提现范围");
        }
        if (memberCashInRecordsService.cashIn(memberId, amount)) {
            throw new BusinessException("提现成功，等待审核");
        } else {
            throw new BusinessException("提现失败，请联系管理员");
        }
    }

    @ApiOperation(value = "用户提现历史记录")
    @RequestMapping(value = "/getUserCashInHistory", method = RequestMethod.GET)
    public ResponseEntity<List<MemberCashInRecords>> getUserCashInHistory() {
        String memberId = UserThread.getInstance().get().getId();
        return new ResponseEntity<>(memberCashInRecordsService.getAllCashInRecords(memberId), HttpStatus.OK);
    }

    @ApiOperation(value = "临时导入收益查询")
    @RequestMapping(value = "/getMemberProfitTmpRecords", method = RequestMethod.GET)
    public ResponseEntity<List<MemberProfitTmpRecords>> getMemberProfitTmpRecords() throws Exception {
        Map<String, String[]> param = new HashMap<>();
        param.put("sort", new String[] {"operateTransactionId"});
        param.put("order", new String[] {"asc"});
        param.put("profitType", new String[] {"2"});
        return new ResponseEntity<>(memberProfitTmpRecordsService.findAll(param), HttpStatus.OK);
    }
}
