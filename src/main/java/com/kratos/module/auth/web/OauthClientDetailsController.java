package com.kratos.module.auth.web;

import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import com.kratos.module.auth.domain.OauthClientDetails;
import com.kratos.module.auth.service.OauthClientDetailsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "应用客户端管理")
@RestController
@RequestMapping("/api/oauthClient")
public class OauthClientDetailsController extends AbstractCrudController<OauthClientDetails> {
    private final OauthClientDetailsService oauthClientDetailsService;

    @Override
    protected CrudService<OauthClientDetails> getService() {
        return oauthClientDetailsService;
    }

    @Autowired
    public OauthClientDetailsController(OauthClientDetailsService oauthClientDetailsService) {
        this.oauthClientDetailsService = oauthClientDetailsService;
    }
}
