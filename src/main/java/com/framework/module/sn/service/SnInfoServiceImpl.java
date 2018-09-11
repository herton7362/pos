package com.framework.module.sn.service;

import com.framework.module.auth.MemberThread;
import com.framework.module.sn.domain.SnInfo;
import com.framework.module.sn.domain.SnInfoHistory;
import com.framework.module.sn.domain.SnInfoRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.AdminThread;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class SnInfoServiceImpl extends AbstractCrudService<SnInfo> implements SnInfoService {

    private final SnInfoRepository snInfoRepository;
    private final SnInfoHistoryService snInfoHistoryService;


    public SnInfoServiceImpl(SnInfoRepository snInfoRepository, SnInfoHistoryService snInfoHistoryService) {
        this.snInfoRepository = snInfoRepository;
        this.snInfoHistoryService = snInfoHistoryService;
    }


    @Override
    public int batchImport(String fileName, MultipartFile profitFile) throws Exception {
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new BusinessException("输入文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = profitFile.getInputStream();
        Workbook wb;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        int importSize = 0;
        Sheet sheet = wb.getSheetAt(0);
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            String sn = row.getCell(0).getStringCellValue();
            if (StringUtils.isBlank(sn)) {
                continue;
            }
            if (snInfoRepository.countAllBySn(sn) > 0) {
                continue;
            }
            SnInfo snInfo = new SnInfo();
            snInfo.setSn(sn);
            save(snInfo);
            importSize++;

            SnInfoHistory snInfoHistory = new SnInfoHistory();
            snInfoHistory.setSn(sn);
            snInfoHistoryService.save(snInfoHistory);
        }
        return importSize;
    }

    @Override
    public void transSnByAdmin(String sns, String memberId) throws Exception {
        String[] snArray = sns.split(",");
        if (snArray.length == 0) {
            throw new BusinessException("sn信息填写的不正确");
        }
        if (snInfoRepository.countAllByMemberId(memberId)<=5 && snArray.length<=5){
            throw new BusinessException("首次划分不得少于5个");
        }
        for (int i = 0; i < snArray.length; i++) {
            SnInfo snInfo = snInfoRepository.findFirstBySn(snArray[i]);
            if (StringUtils.isNotBlank(snInfo.getMemberId())) {
                throw new BusinessException(snArray[i] + "已经划拨给会员" + snInfo.getMemberId());
            }
            snInfo.setMemberId(memberId);
            snInfo.setTransDate(new Date());
            save(snInfo);

            SnInfoHistory snInfoHistory = new SnInfoHistory();
            snInfoHistory.setSn(snInfo.getSn());
            snInfoHistory.setMemberId(snInfo.getMemberId());
            snInfoHistory.setTransDate(snInfo.getTransDate());
            snInfoHistoryService.save(snInfoHistory);

        }
    }

    @Override
    public void transSnByMember(String sns, String memberId, String currentMemberId) throws Exception {
        String[] snArray = sns.split(",");
        if (snArray.length == 0) {
            throw new BusinessException("sn信息填写的不正确");
        }

        if (snInfoRepository.countAllByMemberId(memberId)<=5 && snArray.length<=5){
            throw new BusinessException("首次划分不得少于5个");
        }

        for (int i = 0; i < snArray.length; i++) {
            SnInfo snInfo = snInfoRepository.findFirstBySn(snArray[i]);
            if (!currentMemberId.equals(snInfo.getMemberId())) {
                throw new BusinessException(snArray[i] + "不属于您，不能划拨");
            }
            snInfo.setMemberId(memberId);
            snInfo.setTransDate(new Date());
            save(snInfo);

            SnInfoHistory snInfoHistory = new SnInfoHistory();
            snInfoHistory.setSn(snInfo.getSn());
            snInfoHistory.setMemberId(snInfo.getMemberId());
            snInfoHistory.setTransDate(snInfo.getTransDate());
            snInfoHistoryService.save(snInfoHistory);

        }
    }

    @Override
    public List<String> getAvailableSn() {
        String memberId = MemberThread.getInstance().get().getId();
        return snInfoRepository.getAvailableSnByMemberId(memberId);
    }
}
