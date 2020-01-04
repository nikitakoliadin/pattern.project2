package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.ErrorType;

public class SyncMongoRepositoryException extends GeneralServiceException {

    public SyncMongoRepositoryException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }
}
