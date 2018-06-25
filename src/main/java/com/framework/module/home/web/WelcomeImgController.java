package com.framework.module.home.web;

import com.framework.module.home.domain.WelcomeImg;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "#欢迎界面图片管理")
@RestController
@RequestMapping("/api/welcomeImg")
public class WelcomeImgController extends AbstractCrudController<WelcomeImg> {
}
