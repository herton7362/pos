package com.framework.module.activity.service;

import com.framework.module.activity.domain.AllianceCollege;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class AllianceCollegeServiceImpl extends AbstractCrudService<AllianceCollege> implements AllianceCollegeService {
}
