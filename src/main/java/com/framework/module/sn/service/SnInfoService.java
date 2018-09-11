package com.framework.module.sn.service;

import com.framework.module.sn.domain.SnInfo;
import com.kratos.common.CrudService;
import com.kratos.exceptions.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SnInfoService extends CrudService<SnInfo> {
    /**
     * 批量上传SN
     * @param fileName 文件名称
     * @param profitFile 文件
     * @return 上传文件数量
     */
    int batchImport(String fileName, MultipartFile profitFile) throws Exception;
}
