package com.framework.module.information.web;

import com.framework.module.information.domain.Information;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("消息中心")
@RestController
@RequestMapping("/api/information")
public class InformationController extends AbstractCrudController<Information> {
}
