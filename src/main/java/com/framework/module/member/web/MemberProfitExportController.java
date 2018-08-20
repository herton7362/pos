package com.framework.module.member.web;

import com.framework.module.member.domain.*;
import com.framework.module.member.service.MemberCashInRecordsService;
import com.framework.module.member.service.MemberProfitRecordsService;
import com.framework.module.member.service.MemberService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(value = "会员收益管理")
@RestController
@RequestMapping("/memberprofit")
public class MemberProfitExportController extends AbstractCrudController<MemberProfitRecords> {
    private final MemberProfitRecordsService memberProfitService;
    private final MemberCashInRecordsService memberCashInRecordsService;
    private final MemberService memberService;

    @Override
    protected CrudService<MemberProfitRecords> getService() {
        return memberProfitService;
    }

    @Autowired
    public MemberProfitExportController(
            MemberProfitRecordsService memberProfitService,
            MemberCashInRecordsService memberCashInRecordsService,
            MemberService memberService) {
        this.memberProfitService = memberProfitService;
        this.memberCashInRecordsService = memberCashInRecordsService;
        this.memberService = memberService;
    }


    //生成user表excel
    @ApiOperation(value = "导出提现申请")
    @GetMapping(value = "/exportCashInRecords")
    public ResponseEntity<String> getUser(HttpServletResponse response) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("统计表");
        createTitle(workbook, sheet);
        Map<String, String[]> param = new HashMap<>();
        param.put("status", new String[]{"PASS"});
        List<MemberCashInRecords> rows = memberCashInRecordsService.findAll(param);

        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        for (MemberCashInRecords record : rows) {
            Date cashInDate = new Date(record.getCreatedDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

            HSSFRow row = sheet.createRow(rowNum);
            double tax = record.getCashAmount() * 0.09;
            double cashRealAmount = record.getCashAmount() - tax - 2;
            row.createCell(0).setCellValue(setDouleScale(record.getCashAmount()));
            row.createCell(1).setCellValue(setDouleScale(record.getCashAmount() - tax));
            row.createCell(2).setCellValue(2);
            row.createCell(3).setCellValue(setDouleScale(cashRealAmount));
            row.createCell(4).setCellValue(record.getCollectName());
            row.createCell(5).setCellValue(record.getCollectAccount());
            row.createCell(6).setCellValue(record.getBankName());
            row.createCell(7).setCellValue(sdf.format(cashInDate));
            Member member = memberService.findOne(record.getMemberId());

            row.createCell(8).setCellValue(member.getName());
            row.createCell(9).setCellValue(member.getMobile());
            rowNum++;
        }

        String fileName = "提现申请记录.xls";

        //生成excel文件
        buildExcelFile(fileName, workbook);

        //浏览器下载excel
        buildExcelDocument(fileName, workbook, response);

        return new ResponseEntity<>("导出成功.", HttpStatus.OK);
    }

    //创建表头
    private void createTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 25 * 256);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("提现金额");
        cell.setCellStyle(style);


        cell = row.createCell(1);
        cell.setCellValue("税后金额（税点9%）");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("提现手续费");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("实际打款");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("结算卡账户名");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("结算卡账号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("结算卡开户银行");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("提现日期");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("合伙人姓名");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("合伙人会员宝管家账号");
        cell.setCellStyle(style);
    }

    //生成excel文件
    private void buildExcelFile(String filename, HSSFWorkbook workbook) throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    //浏览器下载excel
    private void buildExcelDocument(String filename, HSSFWorkbook workbook, HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    private double setDouleScale(double inputDouble) {
        return new BigDecimal(inputDouble).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
