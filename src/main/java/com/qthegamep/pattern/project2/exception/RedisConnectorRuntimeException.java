package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.Error;

public class RedisConnectorRuntimeException extends GeneralServiceRuntimeException {

    public RedisConnectorRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
