package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.ErrorType;

public class AsyncMongoDBConnectorRuntimeException extends GeneralServiceRuntimeException {

    public AsyncMongoDBConnectorRuntimeException(ErrorType errorType) {
        super(errorType);
    }

    public AsyncMongoDBConnectorRuntimeException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }
}
