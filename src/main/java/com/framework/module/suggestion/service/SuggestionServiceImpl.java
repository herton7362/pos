package com.framework.module.suggestion.service;

import com.framework.module.suggestion.domain.Suggestion;
import com.kratos.common.AbstractCrudService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class SuggestionServiceImpl extends AbstractCrudService<Suggestion> implements SuggestionService {
}
