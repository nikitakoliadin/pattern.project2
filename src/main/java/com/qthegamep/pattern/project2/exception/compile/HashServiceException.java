package com.qthegamep.pattern.project2.exception.compile;

import com.qthegamep.pattern.project2.model.container.Error;

public class HashServiceException extends GeneralServiceException {

    public HashServiceException(Error error) {
        super(error);
    }

    public HashServiceException(Throwable cause, Error error) {
        super(cause, error);
    }
}
