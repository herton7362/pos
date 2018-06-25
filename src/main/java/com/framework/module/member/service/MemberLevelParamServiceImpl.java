package com.framework.module.member.service;

import com.framework.module.member.domain.*;
import com.kratos.common.AbstractCrudService;
import com.kratos.common.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component("MemberLevelParamService")
@Transactional
public class MemberLevelParamServiceImpl extends AbstractCrudService<MemberLevelParam> implements MemberLevelParamService {
    @Override
    public MemberLevelParam getParamByLevel(String level) throws Exception {
        Map<String, String[]> param = new HashMap<>();
        param.put("level", new String[]{level});
        List<MemberLevelParam> memberLevelParams = findAll(param);
        if (memberLevelParams != null && !memberLevelParams.isEmpty()) {
            return memberLevelParams.get(0);
        }
        return null;
    }
}
