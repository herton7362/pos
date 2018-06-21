package com.kratos.module.auth.domain;

import com.kratos.common.PageRepository;
import org.springframework.data.jpa.repository.Query;

public interface OauthClientDetailsRepository extends PageRepository<OauthClientDetails> {
    @Query("select o from OauthClientDetails o where o.clientId=?1")
    OauthClientDetails findOneByClientId(String clientId);
}
