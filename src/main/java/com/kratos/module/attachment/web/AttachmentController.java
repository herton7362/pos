package com.kratos.module.attachment.web;

import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.attachment.domain.Attachment;
import com.kratos.module.attachment.service.AttachmentService;
import com.kratos.module.attachment.service.AttachmentServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@Api(value = "附件管理")
@RestController
@RequestMapping("/api/attachment")
public class AttachmentController extends AbstractCrudController<Attachment> {
    private final AttachmentService attachmentService;
    @Override
    protected CrudService<Attachment> getService() {
        return attachmentService;
    }

    /**
     * 删除
     */
    @ApiOperation(value="删除")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Attachment> delete(@PathVariable String id) throws Exception {
        ResponseEntity<Attachment> responseEntity;
        Attachment attachment = attachmentService.findOne(id);
        try {
            responseEntity = super.delete(id);
            File temp = new File(AttachmentServiceImpl.prefixPath, attachment.getPath());
            temp.delete();
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new BusinessException("当前附件已经被使用，需要先删除关联数据");
        }
        return responseEntity;
    }

    @Autowired
    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }
}
