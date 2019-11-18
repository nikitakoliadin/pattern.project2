package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.ErrorType;

public class CloseClusterRedisRuntimeException extends GeneralServiceRuntimeException {

    public CloseClusterRedisRuntimeException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }
}
