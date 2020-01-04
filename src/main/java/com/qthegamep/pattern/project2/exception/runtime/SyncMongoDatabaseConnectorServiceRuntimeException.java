package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class SyncMongoDatabaseConnectorServiceRuntimeException extends GeneralServiceRuntimeException {

    public SyncMongoDatabaseConnectorServiceRuntimeException(Error error) {
        super(error);
    }

    public SyncMongoDatabaseConnectorServiceRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
