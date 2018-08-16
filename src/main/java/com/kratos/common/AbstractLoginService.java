package com.kratos.common;

import com.kratos.common.utils.CacheUtils;
import com.kratos.common.utils.SpringUtils;
import com.kratos.common.utils.StringUtils;
import com.kratos.entity.BaseUser;
import com.kratos.exceptions.BusinessException;
import com.kratos.kits.notification.Notification;
import com.kratos.kits.notification.message.SmsVerifyCodeMessage;
import com.kratos.module.auth.UserThread;
import com.kratos.module.auth.domain.Admin;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 提供登录、注册、找回密码、发送验证码等功能
 */
public abstract class AbstractLoginService {
    private TokenStore tokenStore;
    private RestTemplate restTemplate;
    public static String nonePass = "_&^%no_PA$$~";
    public static String fastLogin = "_fastLogin";
    /**
     * 正则表达式：验证手机号
     */
    private static final String REGEX_MOBILE = "^\\d{11}$";

    protected abstract Notification getNotification();

    protected abstract TokenEndpoint getTokenEndpoint();

    /**
     * 发送短信验证码
     *
     * @param mobile 手机号码
     */
    public void sendVerifyCode(String mobile) throws Exception {
        if (!Pattern.matches(REGEX_MOBILE, mobile)) {
            throw new BusinessException(String.format("%s无效的手机号码", mobile));
        }
        BaseUser admin = new Admin();
        admin.setLoginName(mobile);
        admin.setMobile(mobile);
        String code = generateVerifyCode();
        CacheUtils.getInstance().add(mobile, code);
        SmsVerifyCodeMessage message = new SmsVerifyCodeMessage();
        message.setDestUser(admin);
        message.setVerifyCode(code);
        getNotification().send(message);
    }

    /**
     * 修改密码
     *
     * @param mobile   手机号码
     * @param code     短信验证码
     * @param password 密码
     */
    public abstract void editPwd(String mobile, String code, String password) throws Exception;

    /**
     * 登录
     *
     * @param appId     app_id
     * @param appSecret app_secret
     * @param username  手机号码
     * @param password  密码
     * @return {@link OAuth2AccessToken} token
     */
    public ResponseEntity<OAuth2AccessToken> login(String appId, String appSecret, String username, String password) throws Exception {
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("client_id", appId);
        requestParameters.put("client_secret", appSecret);
        requestParameters.put("grant_type", "password");
        requestParameters.put("username", username);
        requestParameters.put("password", password);
        UserThread.getInstance().setClientId(appId);
        Principal principal = new UsernamePasswordAuthenticationToken(new User(appId, appSecret, Collections.emptyList()), null, null);
        return getTokenEndpoint().postAccessToken(principal, requestParameters);
    }

    /**
     * 注册
     *
     * @param mobile   手机号码
     * @param code     短信验证码
     * @param password 密码
     */
    public abstract void register(String mobile, String code, String password, String invitePersonMobile) throws Exception;

    /**
     * 根据手机号获取用户
     *
     * @param mobile 手机号
     * @return {@link BaseUser} 继承了此实体的用户
     */
    public abstract BaseUser findUserByMobile(String mobile) throws Exception;

    /**
     * 生成短信验证码
     *
     * @return 验证码
     */
    private String generateVerifyCode() throws Exception {
        Random r = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(r.nextInt(10));
        }
        return code.toString();
    }

    /**
     * 验证验证码是否正确
     *
     * @param mobile     手机号
     * @param verifyCode 验证码
     * @return 验证码是否正确
     */
    protected Boolean verifyVerifyCode(String mobile, String verifyCode) throws Exception {
        return verifyCode.equals(CacheUtils.getInstance().get(mobile));
    }

    /**
     * 根据手机号删除验证码
     *
     * @param mobile 手机号
     */
    protected void clearVerifyCode(String mobile) throws Exception {
        CacheUtils.getInstance().remove(mobile);
    }

    /**
     * 刷新token
     *
     * @param appId        app_id
     * @param appSecret    app_secret
     * @param refreshToken refresh_token
     */
    public ResponseEntity<OAuth2AccessToken> refreshToken(String appId, String appSecret, String refreshToken) throws Exception {
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("client_id", appId);
        requestParameters.put("client_secret", appSecret);
        requestParameters.put("grant_type", "refresh_token");
        requestParameters.put("refresh_token", refreshToken);
        UserThread.getInstance().setClientId(appId);
        Principal principal = new UsernamePasswordAuthenticationToken(new User(appId, appSecret, Collections.emptyList()), null, null);
        return getTokenEndpoint().postAccessToken(principal, requestParameters);
    }

    /**
     * 获取token
     *
     * @param appId     应用id
     * @param appSecret secret
     * @return token
     */
    public ResponseEntity<OAuth2AccessToken> getAccessToken(String appId, String appSecret) throws Exception {
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("client_id", appId);
        requestParameters.put("client_secret", appSecret);
        requestParameters.put("grant_type", "client_credentials");
        Principal principal = new UsernamePasswordAuthenticationToken(new User(appId, appSecret, Collections.emptyList()), null, null);
        return getTokenEndpoint().postAccessToken(principal, requestParameters);
    }

    /**
     * token登录 如果用户不存在则创建 用户
     *
     * @param appId     应用id
     * @param appSecret 应用secret
     * @param token     token
     * @param username  用户名
     * @return 登录成功信息
     */
    public ResponseEntity<OAuth2AccessToken> loginByToken(String appId, String appSecret, String token, String username) throws Exception {
        tokenStore = SpringUtils.getBean(TokenStore.class);
        restTemplate = SpringUtils.getBean(RestTemplate.class);
        OAuth2AccessToken oAuth2AccessToken = readAccessToken(token);
        if (oAuth2AccessToken == null) {
            throw new BusinessException("token 不正确");
        }
        if(StringUtils.isBlank(username)) {
            throw new BusinessException("username不能为空");
        }
        if (!Pattern.matches(REGEX_MOBILE, username)) {
            throw new BusinessException(String.format("%s无效的手机号码", username));
        }
        BaseUser user = findUserByMobile(username);
        CacheUtils.getInstance().set(username, "123456");
        if (user == null || !"MEMBER".equals(user.getUserType())) {
            register(username, "123456", "123456", null);
        }
        clearVerifyCode(username);
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("client_id", appId);
        requestParameters.put("client_secret", appSecret);
        requestParameters.put("grant_type", "password");
        requestParameters.put("username", username + fastLogin);
        requestParameters.put("password", nonePass);
        UserThread.getInstance().setClientId(appId);
        Principal principal = new UsernamePasswordAuthenticationToken(new User(appId, appSecret, Collections.emptyList()), null, null);
        return getTokenEndpoint().postAccessToken(principal, requestParameters);
    }

    public OAuth2AccessToken readAccessToken(String token) {
        tokenStore = SpringUtils.getBean(TokenStore.class);
        return tokenStore.readAccessToken(token);
    }

    public OAuth2Authentication readAuthentication(String token) {
        tokenStore = SpringUtils.getBean(TokenStore.class);
        return tokenStore.readAuthentication(token);
    }
}

