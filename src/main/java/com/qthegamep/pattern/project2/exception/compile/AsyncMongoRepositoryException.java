package com.qthegamep.pattern.project2.exception.compile;

import com.qthegamep.pattern.project2.model.container.Error;

public class AsyncMongoRepositoryException extends GeneralServiceException {

    public AsyncMongoRepositoryException(Throwable cause, Error error) {
        super(cause, error);
    }
}
