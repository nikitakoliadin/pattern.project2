package com.qthegamep.pattern.project2.exception.compile;

import com.qthegamep.pattern.project2.exception.ServiceException;
import com.qthegamep.pattern.project2.model.container.Error;

public abstract class GeneralServiceException extends Exception implements ServiceException {

    private final Error error;

    GeneralServiceException(Throwable cause) {
        super(cause);
        this.error = Error.UNKNOWN_ERROR;
    }

    GeneralServiceException(Error error) {
        this.error = error;
    }

    GeneralServiceException(Throwable cause, Error error) {
        super(cause);
        this.error = error;
    }

    @Override
    public Error getError() {
        return error;
    }
}
