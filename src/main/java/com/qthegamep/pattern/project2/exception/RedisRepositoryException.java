package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.ErrorType;

public class RedisRepositoryException extends GeneralServiceException {

    public RedisRepositoryException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }
}
