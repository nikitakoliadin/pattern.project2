package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class AsyncMongoDBConnectorRuntimeException extends GeneralServiceRuntimeException {

    public AsyncMongoDBConnectorRuntimeException(Error error) {
        super(error);
    }

    public AsyncMongoDBConnectorRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
