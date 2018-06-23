package com.framework.module.aboutus.web;

import com.framework.module.aboutus.domain.AboutUs;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("关于我们")
@RestController
@RequestMapping("/api/aboutUs")
public class AboutUsController extends AbstractCrudController<AboutUs> {
}
