package com.kratos.module.auth.domain;

import com.kratos.common.PageRepository;
import com.kratos.entity.BaseUser;
import org.springframework.data.jpa.repository.Query;

public interface AdminRepository extends PageRepository<Admin> {
    @Query("select m from Admin m where m.loginName=?1")
    Admin findOneByLoginName(String account);
    @Query("select m from Admin m where m.loginName=?1 and m.clientId=?2")
    BaseUser findOneByLoginNameAndClientId(String account, String clientId);
}
