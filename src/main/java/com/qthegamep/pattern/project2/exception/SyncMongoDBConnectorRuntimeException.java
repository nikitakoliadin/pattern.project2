package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.ErrorType;

public class SyncMongoDBConnectorRuntimeException extends GeneralServiceRuntimeException {

    public SyncMongoDBConnectorRuntimeException(ErrorType errorType) {
        super(errorType);
    }

    public SyncMongoDBConnectorRuntimeException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }
}
