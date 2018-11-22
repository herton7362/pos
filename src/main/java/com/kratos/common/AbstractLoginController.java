package com.kratos.common;

import com.framework.module.auth.MemberThread;
import com.framework.module.member.domain.AllyMemberInfos;
import com.framework.module.member.domain.Member;
import com.framework.module.member.domain.MemberLevelParam;
import com.framework.module.member.service.MemberLevelParamService;
import com.framework.module.member.service.MemberService;
import com.kratos.common.utils.StringUtils;
import com.kratos.entity.BaseUser;
import com.kratos.exceptions.BusinessException;
import com.kratos.module.auth.UserThread;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 提供登录注册等服务
 */
public abstract class AbstractLoginController {
    private final AbstractLoginService loginService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberLevelParamService memberLevelParamService;

    /**
     * 发送短信验证码
     */
    @ApiOperation(value = "发送短信验证码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "mobile", value = "手机号码", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    public ResponseEntity<?> sendVerifyCode(@RequestParam(value = "mobile") String mobile) throws Exception {
        loginService.sendVerifyCode(mobile);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 根据手机号获取用户
     */
    @ApiOperation(value = "根据手机号获取用户")
    @RequestMapping(value = "/user/mobile/{mobile}", method = RequestMethod.GET)
    public ResponseEntity<BaseUser> findUserByMobile(@PathVariable(value = "mobile") String mobile) throws Exception {
        return new ResponseEntity<>(loginService.findUserByMobile(mobile), HttpStatus.OK);
    }

    /**
     * token登录
     */
    @ApiOperation(value = "token登录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "appId", value = "app_id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "appSecret", value = "app_secret", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "token", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "手机号码", dataType = "String", paramType = "query")

    })
    @RequestMapping(value = "/token/login", method = {RequestMethod.POST})
    public ResponseEntity<OAuth2AccessToken> loginByToken(
            @RequestParam(value = "appId") String appId,
            @RequestParam(value = "appSecret") String appSecret,
            @RequestParam(value = "token") String token,
            @RequestParam(value = "username") String username
    ) throws Exception {
        ResponseEntity<OAuth2AccessToken> responseEntity;
        responseEntity = loginService.loginByToken(appId, appSecret, token, username);
        return responseEntity;
    }

    /**
     * 修改密码
     */
    @ApiOperation(value = "修改密码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "mobile", value = "手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "repassword", value = "确认密码", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/editPwd", method = RequestMethod.POST)
    public ResponseEntity<?> editPwd(
            @RequestParam(value = "mobile") String mobile,
            @RequestParam(value = "code") String code,
            @RequestParam(value = "password") String password
    ) throws Exception {
        loginService.editPwd(mobile, code, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 刷新token
     */
    @ApiOperation(value = "刷新token")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "appId", value = "app_id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "appSecret", value = "app_secret", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "refreshToken", value = "refresh_token", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/refresh/token", method = RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken> refreshToken(
            @RequestParam(value = "appId") String appId,
            @RequestParam(value = "appSecret") String appSecret,
            @RequestParam(value = "refreshToken") String refreshToken
    ) throws Exception {
        ResponseEntity<OAuth2AccessToken> responseEntity;
        try {
            responseEntity = loginService.refreshToken(appId, appSecret, refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
        return responseEntity;
    }

    /**
     * 登录
     */
    @ApiOperation(value = "登录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", value = "手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/admin/login", method = {RequestMethod.POST})
    public ResponseEntity<OAuth2AccessToken> login(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ) throws Exception {
        ResponseEntity<OAuth2AccessToken> responseEntity;
        try {
            String appId = "tonr";
            String appSecret = "secret";
            responseEntity = loginService.login(appId, appSecret, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
        return responseEntity;
    }

    /**
     * 登录
     */
    @ApiOperation(value = "登录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "appId", value = "app_id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "appSecret", value = "app_secret", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken> login(
            @RequestParam(value = "appId") String appId,
            @RequestParam(value = "appSecret") String appSecret,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ) throws Exception {
        ResponseEntity<OAuth2AccessToken> responseEntity;
        try {
            responseEntity = loginService.login(appId, appSecret, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
        return responseEntity;
    }

    /**
     * 注册
     */
    @ApiOperation(value = "注册")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "mobile", value = "手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "invitePersonMobile", value = "邀请人手机号", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(
            @RequestParam(value = "mobile") String mobile,
            @RequestParam(value = "code") String code,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "invitePersonMobile") String invitePersonMobile
    ) throws Exception {
        loginService.register(mobile, code, password, invitePersonMobile);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 获取token
     */
    @ApiOperation(value = "获取token")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "appId", value = "app_id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "appSecret", value = "app_secret", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/login/code", method = RequestMethod.POST)
    public ResponseEntity<?> loginByCode(
            @RequestParam(value = "appId") String appId,
            @RequestParam(value = "appSecret") String appSecret
    ) throws Exception {
        ResponseEntity<OAuth2AccessToken> responseEntity;
        try {
            responseEntity = loginService.getAccessToken(appId, appSecret);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
        return responseEntity;
    }

    /**
     * 验证码校验
     */
    @ApiOperation(value = "验证码校验")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "mobile", value = "手机号码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/verify/verifyCode", method = RequestMethod.GET)
    public ResponseEntity<Boolean> verifyVerifyCode(
            @RequestParam(value = "mobile") String mobile,
            @RequestParam(value = "code") String code
    ) throws Exception {
        return new ResponseEntity<>(loginService.verifyVerifyCode(mobile, code), HttpStatus.OK);
    }

    /**
     * 查询登录用户
     */
    @ApiOperation(value = "查询登录用户")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "登录返回token", name = "access_token", dataType = "String", paramType = "query")})
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public ResponseEntity<BaseUser> getOne() throws Exception {
        BaseUser user = UserThread.getInstance().get();
        if (user instanceof Member) {
            Member member = (Member) user;
            if (StringUtils.isNotBlank(member.getFatherId())) {
                Member father = memberService.findOne(member.getFatherId());
                if (father != null) {
                    member.setFatherMobile(father.getLoginName());
                    member.setFatherName(father.getName());
                }
            }
            AllyMemberInfos allyMembers = memberService.getAlliesInfosByMemberId(member.getId(), new Date().getTime());
            int activeSonNum = 0;
            int activeGrandSonNum = 0;
            if (allyMembers != null) {
                member.setAllySonNumber(allyMembers.getSonList().size());
                member.setAllyAllNumber(allyMembers.getTotalNum());
                for (Member m : allyMembers.getSonList()) {
                    if (Member.Status.ACTIVE.equals(m.getStatus())) {
                        activeSonNum++;
                    }
                }
                for (Member m : allyMembers.getGrandSonList()) {
                    if (Member.Status.ACTIVE.equals(m.getStatus())) {
                        activeGrandSonNum++;
                    }
                }
            }
            member.setAllyAllActiveNumber(activeGrandSonNum + activeSonNum);
            member.setAllySonActiveNumber(activeSonNum);
            MemberLevelParam levelParam = memberLevelParamService.getParamByLevel(String.valueOf(member.getMemberLevel()));
            if (levelParam != null) {
                member.setMemberLevelName(levelParam.getLevelName());
            }
            return new ResponseEntity<>(member, HttpStatus.OK);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * 查询登录用户
     */
    @ApiOperation(value = "查询用户激活子孙数量")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "登录返回token", name = "access_token", dataType = "String", paramType = "query")})
    @RequestMapping(value = "/user/getActiveSonNum", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Integer>> getActiveSonNum() throws Exception {
        Map<String, Integer> result = new HashMap<>();
        Member member = MemberThread.getInstance().get();
        AllyMemberInfos allyMembers = memberService.getAlliesInfosByMemberId(member.getId(), new Date().getTime());
        int activeSonNum = 0;
        int activeGrandSonNum = 0;
        if (allyMembers != null) {
            result.put("allySonNumber", allyMembers.getSonList().size());
            result.put("allyAllNumber", allyMembers.getTotalNum());
            for (Member m : allyMembers.getSonList()) {
                if (Member.Status.ACTIVE.equals(m.getStatus())) {
                    activeSonNum++;
                }
            }
            for (Member m : allyMembers.getGrandSonList()) {
                if (Member.Status.ACTIVE.equals(m.getStatus())) {
                    activeGrandSonNum++;
                }
            }
        }
        result.put("allyAllActiveNumber", activeGrandSonNum + activeSonNum);
        result.put("allySonActiveNumber", activeSonNum);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 获取OAuth2AccessToken
     */
    @ApiOperation(value = "获取OAuth2AccessToken")
    @RequestMapping(value = "/token/{token}", method = RequestMethod.GET)
    public ResponseEntity<OAuth2AccessToken> getToken(@PathVariable String token) throws Exception {
        return new ResponseEntity<>(loginService.readAccessToken(token), HttpStatus.OK);
    }

    /**
     * 获取OAuth2AccessToken
     */
    @ApiOperation(value = "获取OAuth2AccessToken")
    @RequestMapping(value = "/token/authentication/{token}", method = RequestMethod.GET)
    public ResponseEntity<OAuth2Authentication> readAuthentication(@PathVariable String token) throws Exception {
        return new ResponseEntity<>(loginService.readAuthentication(token), HttpStatus.OK);
    }

    public AbstractLoginController(AbstractLoginService loginService) {
        this.loginService = loginService;
    }
}
