package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class SyncMongoDBConnectorRuntimeException extends GeneralServiceRuntimeException {

    public SyncMongoDBConnectorRuntimeException(Error error) {
        super(error);
    }

    public SyncMongoDBConnectorRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
