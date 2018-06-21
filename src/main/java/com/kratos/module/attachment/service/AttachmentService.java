package com.kratos.module.attachment.service;

import com.kratos.common.CrudService;
import com.kratos.module.attachment.domain.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService extends CrudService<Attachment> {
    /**
     * 保存
     * @param multipartFile 待保存文件
     * @return 保存好的实体
     */
    Attachment save(MultipartFile multipartFile) throws Exception;

    /**
     * 批量保存
     * @param multipartFiles 待保存文件
     * @return 保存好的实体
     */
    List<Attachment> save(List<MultipartFile> multipartFiles) throws Exception;
}
