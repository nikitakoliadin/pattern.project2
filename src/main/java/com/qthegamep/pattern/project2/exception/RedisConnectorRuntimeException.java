package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.ErrorType;

public class RedisConnectorRuntimeException extends GeneralServiceRuntimeException {

    public RedisConnectorRuntimeException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }
}
