package com.qthegamep.pattern.project2.exception.compile;

import com.qthegamep.pattern.project2.exception.ServiceException;
import com.qthegamep.pattern.project2.model.container.Error;

abstract class GeneralServiceException extends Exception implements ServiceException {

    private final Error error;

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
