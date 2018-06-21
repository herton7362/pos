package com.kratos.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

public class RestExceptionMessage extends ExceptionMessage {
    @Override
    public ModelAndView parse(Exception e) {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(jsonView);
        Map<String, Object> message = getMessage(e);
        modelAndView.getModelMap().putAll(message);
        modelAndView.setStatus(HttpStatus.valueOf(getStatus()));
        return modelAndView;
    }
}
