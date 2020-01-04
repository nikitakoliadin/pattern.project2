package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class CloseRedisClustersDatabaseConnectorServiceRuntimeException extends GeneralServiceRuntimeException {

    public CloseRedisClustersDatabaseConnectorServiceRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
