package com.kratos.exceptions;

import javax.servlet.http.HttpServletRequest;

class ExceptionMessageProvider {
    private ExceptionMessageProvider() {}
    static ExceptionMessage getExceptionMessage(HttpServletRequest request) {
        if(isAjax(request)) {
            return new RestExceptionMessage();
        }
        return new TraditionalExceptionMessage();
    }
    private static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }
}
