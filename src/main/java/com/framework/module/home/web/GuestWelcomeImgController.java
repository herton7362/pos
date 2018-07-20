package com.framework.module.home.web;

import com.framework.module.home.domain.WelcomeImg;
import com.kratos.common.AbstractReadController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "#欢迎界面图片不用登陆")
@RestController
@RequestMapping("/welcomeImg")
public class GuestWelcomeImgController extends AbstractReadController<WelcomeImg> {
}
