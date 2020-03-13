package com.qthegamep.pattern.project2.exception.compile;

import com.qthegamep.pattern.project2.model.container.Error;

public class MongoRepositorySyncException extends GeneralServiceException {

    public MongoRepositorySyncException(Throwable cause, Error error) {
        super(cause, error);
    }
}
