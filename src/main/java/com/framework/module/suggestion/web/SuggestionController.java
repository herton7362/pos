package com.framework.module.suggestion.web;

import com.framework.module.suggestion.domain.Suggestion;
import com.kratos.common.AbstractCrudController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("#问题建议")
@RestController
@RequestMapping("/api/suggestion")
public class SuggestionController extends AbstractCrudController<Suggestion> {
}
