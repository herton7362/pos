package com.framework.module.home.web;

import com.framework.module.home.domain.HelpImg;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "首页帮助图片管理")
@RestController
@RequestMapping("/api/helpImg")
public class HelpImgController extends AbstractCrudController<HelpImg> {
}
