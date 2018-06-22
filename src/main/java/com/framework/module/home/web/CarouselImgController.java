package com.framework.module.home.web;

import com.framework.module.home.domain.CarouselImg;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "首页轮播图片管理")
@RestController
@RequestMapping("/api/carouselImg")
public class CarouselImgController extends AbstractCrudController<CarouselImg> {
}
