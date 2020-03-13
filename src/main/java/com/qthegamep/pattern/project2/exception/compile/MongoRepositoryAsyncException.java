package com.qthegamep.pattern.project2.exception.compile;

import com.qthegamep.pattern.project2.model.container.Error;

public class MongoRepositoryAsyncException extends GeneralServiceException {

    public MongoRepositoryAsyncException(Throwable cause, Error error) {
        super(cause, error);
    }
}
