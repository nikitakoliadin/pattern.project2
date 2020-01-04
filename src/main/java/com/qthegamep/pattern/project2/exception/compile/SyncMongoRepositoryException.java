package com.qthegamep.pattern.project2.exception.compile;

import com.qthegamep.pattern.project2.model.container.Error;

public class SyncMongoRepositoryException extends GeneralServiceException {

    public SyncMongoRepositoryException(Throwable cause, Error error) {
        super(cause, error);
    }
}
