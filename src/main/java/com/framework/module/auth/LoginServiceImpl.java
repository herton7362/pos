package com.framework.module.auth;

import com.framework.module.member.domain.Member;
import com.framework.module.member.service.MemberService;
import com.kratos.common.AbstractLoginService;
import com.kratos.common.utils.StringUtils;
import com.kratos.entity.BaseUser;
import com.kratos.exceptions.BusinessException;
import com.kratos.kits.Kits;
import com.kratos.kits.notification.Notification;
import com.kratos.module.attachment.domain.Attachment;
import com.kratos.module.attachment.service.AttachmentService;
import com.kratos.module.auth.domain.Admin;
import com.kratos.module.auth.domain.Role;
import com.kratos.module.auth.service.AdminService;
import com.kratos.module.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class LoginServiceImpl extends AbstractLoginService {
    private final Kits kits;
    private final TokenEndpoint tokenEndpoint;
    private final MemberService memberService;
    private final AdminService adminService;
    private final AttachmentService attachmentService;
    private final RoleService roleService;

    @Override
    protected Notification getNotification() {
        return kits.notification();
    }

    @Override
    protected TokenEndpoint getTokenEndpoint() {
        return tokenEndpoint;
    }

    @Override
    public void editPwd(String mobile, String code, String password) throws Exception {
        if (!verifyVerifyCode(mobile, code)) {
            throw new BusinessException(String.format("验证码%s不正确", code));
        }
        Member member = memberService.findOneByLoginName(mobile);
        if (member == null) {
            throw new BusinessException("当前号码未注册");
        }
        member.setPassword(password);
        memberService.editPwd(member);
    }

    @Override
    public void register(String mobile, String code, String password, String invitePersonMobile) throws Exception {
        if (!verifyVerifyCode(mobile, code)) {
            throw new BusinessException(String.format("验证码%s不正确", code));
        }
        String reg = "^[A-Za-z0-9]{4,40}$";
        if (StringUtils.isBlank(password)) {
            throw new BusinessException("密码不能为空");
        }
        if (!password.matches(reg)) {
            throw new BusinessException("");
        }
        if (findUserByMobile(mobile) != null) {
            throw new BusinessException("该手机号已被注册，请选择找回密码或者直接登录");
        }
        if (StringUtils.isBlank(invitePersonMobile)) {
            throw new BusinessException("邀请人手机号不能为空");
        }
        Member father = null;
        if (StringUtils.isNotBlank(invitePersonMobile)) {
            father = memberService.findOneByLoginName(invitePersonMobile);
            if (father == null) {
                throw new BusinessException("邀请人手机号不正确");
            }
        }

        Member member = new Member();
        if (father != null) {
            member.setFatherId(father.getId());
        }
        member.setMobile(mobile);
        member.setLoginName(mobile);
        member.setPassword(new BCryptPasswordEncoder().encode(password));
        memberService.save(member);
        clearVerifyCode(mobile);

        Attachment photoAttachment = attachmentService.findOne("00000000645eb26601645eb364130000");
        Role role = roleService.findOne("00000000660fd3f401661dc161fe0970");
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        Admin admin = new Admin();
        admin.setLoginName("m" + mobile);
        admin.setPassword(password);
        admin.setMemberId(member.getId());
        admin.setMobile(mobile);
        admin.setName(mobile);
        admin.setClientId("tonr");
        if (photoAttachment != null) {
            admin.setHeadPhoto(photoAttachment);
        }
        admin.setRoles(roleList);
        adminService.save(admin);

    }

    @Override
    public BaseUser findUserByMobile(String mobile) {
        return memberService.findOneByLoginName(mobile);
    }

    @Autowired
    public LoginServiceImpl(
            Kits kits,
            TokenEndpoint tokenEndpoint,
            MemberService memberService,
            AdminService adminService, AttachmentService attachmentService, RoleService roleService) {
        this.kits = kits;
        this.tokenEndpoint = tokenEndpoint;
        this.memberService = memberService;
        this.adminService = adminService;
        this.attachmentService = attachmentService;
        this.roleService = roleService;
    }
}
