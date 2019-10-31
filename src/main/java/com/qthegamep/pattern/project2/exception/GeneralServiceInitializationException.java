package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.ErrorType;

abstract class GeneralServiceInitializationException extends Exception implements ServiceException {

    private final ErrorType errorType;

    GeneralServiceInitializationException(Throwable cause) {
        super(cause);
        this.errorType = ErrorType.UNKNOWN_ERROR;
    }

    @Override
    public ErrorType getErrorType() {
        return errorType;
    }
}
