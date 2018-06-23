package com.framework.module.aboutus.service;

import com.framework.module.aboutus.domain.AboutUs;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class AboutUsServiceImpl extends AbstractCrudService<AboutUs> implements AboutUsService {
}
