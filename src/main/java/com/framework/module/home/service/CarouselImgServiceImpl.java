package com.framework.module.home.service;

import com.framework.module.home.domain.CarouselImg;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CarouselImgServiceImpl extends AbstractCrudService<CarouselImg> implements CarouselImgService {
}
