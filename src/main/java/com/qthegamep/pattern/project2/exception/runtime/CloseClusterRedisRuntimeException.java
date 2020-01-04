package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class CloseClusterRedisRuntimeException extends GeneralServiceRuntimeException {

    public CloseClusterRedisRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
