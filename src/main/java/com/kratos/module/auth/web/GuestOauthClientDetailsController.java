package com.kratos.module.auth.web;

import com.kratos.common.AbstractReadController;
import com.kratos.common.CrudService;
import com.kratos.module.auth.domain.OauthClientDetails;
import com.kratos.module.auth.service.OauthClientDetailsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "游客应用客户端管理接口，无权限过滤")
@RestController
@RequestMapping("/oauthClient")
public class GuestOauthClientDetailsController extends AbstractReadController<OauthClientDetails> {
    private final OauthClientDetailsService oauthClientDetailsService;
    @Override
    protected CrudService<OauthClientDetails> getService() {
        return oauthClientDetailsService;
    }

    @Autowired
    public GuestOauthClientDetailsController(OauthClientDetailsService oauthClientDetailsService) {
        this.oauthClientDetailsService = oauthClientDetailsService;
    }
}
