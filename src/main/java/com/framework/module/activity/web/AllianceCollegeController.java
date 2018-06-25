package com.framework.module.activity.web;

import com.framework.module.activity.domain.AllianceCollege;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "#联盟学院管理")
@RestController
@RequestMapping("/api/allianceCollege")
public class AllianceCollegeController extends AbstractCrudController<AllianceCollege> {
}
