package com.framework.module.home.web;

import com.framework.module.home.domain.HelpImg;
import com.kratos.common.AbstractReadController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "#首页帮助图片不用登陆")
@RestController
@RequestMapping("/helpImg")
public class GuestHelpImgController extends AbstractReadController<HelpImg> {
}
