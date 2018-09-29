package com.framework.module.sn.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.framework.module.member.domain.Member;
import com.framework.module.member.service.MemberService;
import com.framework.module.sn.domain.SnInfoRepository;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.attachment.domain.Attachment;
import com.kratos.module.attachment.service.AttachmentService;
import com.kratos.module.auth.AdminThread;
import com.kratos.module.auth.domain.Admin;
import com.kratos.module.auth.domain.Role;
import com.kratos.module.auth.service.AdminService;
import com.kratos.module.auth.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.framework.module.sn.domain.SnInfo;
import com.framework.module.sn.service.SnInfoService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.PageResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "SN管理")
@RestController
@RequestMapping("/api/sn")
public class SnController extends AbstractCrudController<SnInfo> {
    private final SnInfoService snInfoService;
    private final AdminService adminService;
    private final AttachmentService attachmentService;
    private final RoleService roleService;
    private final MemberService memberService;
    private final SnInfoRepository snInfoRepository;

    public SnController(SnInfoService snInfoService, AdminService adminService, AttachmentService attachmentService, RoleService roleService, MemberService memberService, SnInfoRepository snInfoRepository) {
        this.snInfoService = snInfoService;
        this.adminService = adminService;
        this.attachmentService = attachmentService;
        this.roleService = roleService;
        this.memberService = memberService;
        this.snInfoRepository = snInfoRepository;
    }

    @Override
    public ResponseEntity<SnInfo> save(@RequestBody SnInfo snInfo) throws Exception {
        SnInfo oleSn = snInfoRepository.findFirstBySn(snInfo.getSn());
        if (oleSn != null && !oleSn.getId().equals(snInfo.getId())) {
            throw new BusinessException("重复的SN数据");
        }
        snInfo = crudService.save(snInfo);
        return new ResponseEntity<>(snInfo, HttpStatus.OK);
    }

    /**
     * 导入SN信息
     */
    @ApiOperation(value = "导入sn信息")
    @RequestMapping(value = "/importSn", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity<String> importSn(@RequestParam("snFile") MultipartFile snFile) {
        String fileName = snFile.getOriginalFilename();
        int insertSize;
        try {
            insertSize = snInfoService.batchImport(fileName, snFile);
        } catch (Exception e) {
            return new ResponseEntity<>("上传失败，原因是:" + e.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>("上传成功." + insertSize + "条数据被上传.", HttpStatus.OK);
    }

    /**
     * 管理员划拨未分配的SN
     */
    @ApiOperation(value = "管理员划拨未分配的SN")
    @RequestMapping(value = "/transSnByAdmin", method = RequestMethod.POST)
    public ResponseEntity<?> transSnByAdmin(@RequestBody Map<String, String> reqMap) throws Exception {
        String sns = reqMap.get("sns");
        String memberId = reqMap.get("memberId");
        snInfoService.transSnByAdmin(sns, memberId);
        productAdmin(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 会员划拨给儿子
     */
    @ApiOperation(value = "会员划拨给儿子")
    @RequestMapping(value = "/transSnByMember", method = RequestMethod.POST)
    public ResponseEntity<?> transSnByMember(@RequestBody Map<String, String> reqMap) throws Exception {
        String sns = reqMap.get("sns");
        String memberId = reqMap.get("memberId");
        String currentMemberId = AdminThread.getInstance().get().getMemberId();
        snInfoService.transSnByMember(sns, memberId, currentMemberId);
        productAdmin(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 查询会员下可分配的SN信息
     */
    @ApiOperation(value = "查询会员下可分配的SN信息")
    @RequestMapping(value = "/getAvailableSn", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getAvailableSn() {
        return new ResponseEntity<>(snInfoService.getAvailableSn(), HttpStatus.OK);
    }

    @ApiOperation(value = "查询所有SN信息，有筛选功能")
    @RequestMapping(value = "/getAllSnInfo", method = RequestMethod.GET)
    public ResponseEntity<PageResult<SnInfo>> getAllSnInfo(@RequestParam(required = false) String startSn, @RequestParam(required = false) String endSn, @RequestParam(required = false) SnInfo.Status status, @RequestParam(required = false) SnInfo.BindStatus bindStatus, @RequestParam() Integer pageSize, @RequestParam() Integer pageNum) throws Exception {
        String memberId = AdminThread.getInstance().get().getMemberId();
        return new ResponseEntity<>(snInfoService.getAllSnInfo(startSn, endSn, status, bindStatus, pageSize, pageNum, memberId), HttpStatus.OK);
    }

    @ApiOperation(value = "查询待分配列表")
    @RequestMapping(value = "/getUnDistributionList", method = RequestMethod.GET)
    public ResponseEntity<List<SnInfo>> getUnDistributionList(@RequestParam String memberId) {
        List<SnInfo> result = snInfoService.getUnDistributionList(memberId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private void productAdmin(String memberId) throws Exception {
        Member member = memberService.findOne(memberId);
        if (member == null) {
            return;
        }
        Admin admin = adminService.findByMemberId(member.getId());
        if (admin != null) {
            return;
        }
        Attachment photoAttachment = attachmentService.findOne("00000000645eb26601645eb364130000");
        Role role = roleService.findOne("00000000660fd3f401661dc161fe0970");
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        admin = new Admin();
        admin.setLoginName("m" + member.getMobile());
        admin.setPassword("123456");
        admin.setMemberId(member.getId());
        admin.setMobile(member.getMobile());
        admin.setName(member.getMobile());
        admin.setClientId("tonr");
        if (photoAttachment != null) {
            admin.setHeadPhoto(photoAttachment);
        }
        admin.setRoles(roleList);
        adminService.save(admin);
    }
}
