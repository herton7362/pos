package com.framework.module.sn.service;

import com.framework.module.sn.domain.SnInfo;
import com.framework.module.sn.domain.SnInfoRepository;
import com.kratos.common.AbstractCrudService;
import com.kratos.exceptions.BusinessException;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class SnInfoServiceImpl extends AbstractCrudService<SnInfo> implements SnInfoService {

    private final SnInfoRepository snInfoRepository;

    public SnInfoServiceImpl(SnInfoRepository snInfoRepository) {
        this.snInfoRepository = snInfoRepository;
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
        }
        return importSize;
    }
}
