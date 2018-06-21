package com.kratos.config;

import com.kratos.common.utils.NetworkUtils;
import com.kratos.common.utils.SpringUtils;
import com.kratos.common.utils.StringUtils;
import com.kratos.entity.BaseUser;
import com.kratos.module.auth.UserThread;
import com.kratos.module.auth.domain.AdminRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonInterceptor extends HandlerInterceptorAdapter {
    private AdminRepository adminRepository;
    private TokenStore tokenStore;
    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        adminRepository = SpringUtils.getBean(AdminRepository.class);
        tokenStore = SpringUtils.getBean(TokenStore.class);
        String accessToken = request.getParameter("access_token");
        UserThread.getInstance().setAccessToken(accessToken);
        UserThread.getInstance().setIpAddress(NetworkUtils.getIpAddress(request));
        if(StringUtils.isNotBlank(accessToken)) {
            OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
            if(oAuth2Authentication != null) {
                Object principal = oAuth2Authentication.getPrincipal();

                if(principal instanceof User) {
                    User user = (User) principal;
                    UserThread.getInstance().setClientId(oAuth2Authentication.getOAuth2Request().getClientId());
                    BaseUser baseUser = adminRepository.findOneByLoginNameAndClientId(user.getUsername(), oAuth2Authentication.getOAuth2Request().getClientId());
                    if(baseUser != null) {
                        baseUser.setPassword(null);
                        UserThread.getInstance().set(baseUser);
                    } else {
                        UserThread.getInstance().set(null);
                        UserThread.getInstance().setClientId(null);
                        UserThread.getInstance().setAccessToken(null);
                    }
                } else if(principal instanceof String) {
                    UserThread.getInstance().setClientId((String) principal);
                }
            } else {
                UserThread.getInstance().set(null);
                UserThread.getInstance().setClientId(null);
                UserThread.getInstance().setAccessToken(null);
            }
        } else {
            UserThread.getInstance().set(null);
            UserThread.getInstance().setClientId(null);
            UserThread.getInstance().setAccessToken(null);
        }
        setHeaders(response);
        return true;
    }

    private void setHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    }
}
