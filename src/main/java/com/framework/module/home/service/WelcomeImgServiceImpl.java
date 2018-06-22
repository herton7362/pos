package com.framework.module.home.service;

import com.framework.module.home.domain.WelcomeImg;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class WelcomeImgServiceImpl extends AbstractCrudService<WelcomeImg> implements WelcomeImgService {
}
