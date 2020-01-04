package com.qthegamep.pattern.project2.exception.initialization;

import com.qthegamep.pattern.project2.exception.ServiceException;
import com.qthegamep.pattern.project2.model.container.Error;

abstract class GeneralServiceInitializationException extends Exception implements ServiceException {

    private final Error error;

    GeneralServiceInitializationException(Throwable cause) {
        super(cause);
        this.error = Error.UNKNOWN_ERROR;
    }

    @Override
    public Error getError() {
        return error;
    }
}
