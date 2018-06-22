package com.framework.module.home.service;

import com.framework.module.home.domain.HelpImg;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class HelpImgServiceImpl extends AbstractCrudService<HelpImg> implements HelpImgService {
}
