package com.kratos.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Base Exception
 * 
 * @author tang he
 * @since 1.0.0
 */
public class BusinessException extends Exception implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(BusinessException.class);
    private static final long serialVersionUID = 6807228021289834441L;
    private static final String DEFAULT_MASSAGES = "出现业务异常，未标明业务异常信息";
    private BusinessException() {}

    public BusinessException(String message) {
        super(message);
        LOG.warn(message);
    }
}
