package com.framework.module.activity.web;

import com.framework.module.activity.domain.ActivityImg;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "活动图片管理")
@RestController
@RequestMapping("/api/activityImg")
public class ActivityImgController extends AbstractCrudController<ActivityImg> {
}
