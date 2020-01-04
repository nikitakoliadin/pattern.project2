package com.qthegamep.pattern.project2.exception.compile;

import com.qthegamep.pattern.project2.model.container.Error;

public class RedisRepositoryException extends GeneralServiceException {

    public RedisRepositoryException(Throwable cause, Error error) {
        super(cause, error);
    }
}
