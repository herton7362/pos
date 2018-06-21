package com.kratos.exceptions;

import org.springframework.web.servlet.ModelAndView;

public class TraditionalExceptionMessage extends ExceptionMessage {

    @Override
    public ModelAndView parse(Exception e) {
        return new ModelAndView("error", getMessage(e));
    }
}
