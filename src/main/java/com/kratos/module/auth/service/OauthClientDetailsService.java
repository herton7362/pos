package com.kratos.module.auth.service;

import com.kratos.common.CrudService;
import com.kratos.module.auth.domain.OauthClientDetails;

public interface OauthClientDetailsService extends CrudService<OauthClientDetails> {
    OauthClientDetails findOneByClientId(String clientId) throws Exception;
}
