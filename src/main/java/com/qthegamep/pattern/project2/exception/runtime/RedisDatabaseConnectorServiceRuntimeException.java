package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class RedisDatabaseConnectorServiceRuntimeException extends GeneralServiceRuntimeException {

    public RedisDatabaseConnectorServiceRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
