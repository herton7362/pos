package com.framework.module.member.web;

import com.kratos.module.auth.AdminThread;
import com.kratos.module.auth.domain.Admin;
import com.kratos.module.auth.domain.AdminRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("后台管理绑定会员")
@RestController
@RequestMapping("/api/bindMember")
public class BindMemberController {

    private final AdminRepository adminRepository;

    public BindMemberController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @ApiOperation(value = "导入sn信息")
    @RequestMapping(value = "/bind", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity<String> bind(@RequestParam() String memberId) {
        Admin admin = adminRepository.findOne(AdminThread.getInstance().get().getId());
        if (admin == null) {
            return new ResponseEntity<>("无用户信息", HttpStatus.BAD_REQUEST);
        }
        admin.setMemberId(memberId);
        adminRepository.save(admin);
        return new ResponseEntity<>("绑定成功.", HttpStatus.OK);
    }
}
