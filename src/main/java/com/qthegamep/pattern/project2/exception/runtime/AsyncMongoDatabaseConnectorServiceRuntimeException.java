package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class AsyncMongoDatabaseConnectorServiceRuntimeException extends GeneralServiceRuntimeException {

    public AsyncMongoDatabaseConnectorServiceRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
