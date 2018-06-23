package com.framework.module.information.service;

import com.framework.module.information.domain.Information;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class InformationServiceImpl extends AbstractCrudService<Information> implements InformationService {
}
