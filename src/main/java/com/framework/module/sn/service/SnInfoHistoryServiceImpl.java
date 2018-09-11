package com.framework.module.sn.service;

import com.framework.module.sn.domain.SnInfoHistory;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class SnInfoHistoryServiceImpl extends AbstractCrudService<SnInfoHistory> implements SnInfoHistoryService {
}
