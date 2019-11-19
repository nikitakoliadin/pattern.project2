package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.ErrorType;

public class AsyncMongoRepositoryException extends GeneralServiceException {

    public AsyncMongoRepositoryException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }
}
