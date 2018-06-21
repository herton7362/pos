package com.kratos.module.attachment.web;

import com.kratos.common.AbstractReadController;
import com.kratos.common.CrudService;
import com.kratos.common.utils.OSUtils;
import com.kratos.module.attachment.domain.Attachment;
import com.kratos.module.attachment.service.AttachmentService;
import com.kratos.module.attachment.service.AttachmentServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Api(value = "游客附件接口，无权限过滤")
@RestController
@RequestMapping("/attachment")
public class GuestAttachmentController extends AbstractReadController<Attachment> {
    private final Logger LOG = LoggerFactory.getLogger(GuestAttachmentController.class);
    private final AttachmentService attachmentService;
    @Override
    protected CrudService<Attachment> getService() {
        return attachmentService;
    }

    /**
     * 上传文件
     */
    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public ResponseEntity<List<Attachment>> upload(@RequestParam("attachments") MultipartFile[] attachments) throws Exception {
        List<Attachment> results = attachmentService.save(Arrays.asList(attachments));
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    /**
     * 下载文件
     */
    @ApiOperation(value="下载文件")
    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable String id) throws Exception {
        Attachment attachment = attachmentService.findOne(id);
        String prefixPath = null;
        if(OSUtils.isWindows()) {
            prefixPath = AttachmentServiceImpl.prefixPath;
        }
        if(attachment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        File file=new File(prefixPath, attachment.getPath());
        HttpHeaders headers=new HttpHeaders();
        String downloadFileName=new String(attachment.getName().getBytes("UTF-8"),"ISO-8859-1");  //少了这句，可能导致下载中文文件名的文档，只有后缀名的情况
        headers.setContentDispositionFormData("attachment", downloadFileName);//告知浏览器以下载方式打开
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);//设置MIME类型
        byte[] bytes = null;
        try {
            bytes = FileUtils.readFileToByteArray(file);
        } catch (Exception e) {
            LOG.debug("未找到文件", e);
        }
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @Autowired
    public GuestAttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }
}
