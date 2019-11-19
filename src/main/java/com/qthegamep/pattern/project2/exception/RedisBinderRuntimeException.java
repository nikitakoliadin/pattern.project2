package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.ErrorType;

public class RedisBinderRuntimeException extends GeneralServiceRuntimeException {

    public RedisBinderRuntimeException(ErrorType errorType) {
        super(errorType);
    }
}
