package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.ErrorType;

abstract class GeneralServiceException extends Exception implements ServiceException {

    private final ErrorType errorType;

    GeneralServiceException(ErrorType errorType) {
        this.errorType = errorType;
    }

    GeneralServiceException(Throwable cause, ErrorType errorType) {
        super(cause);
        this.errorType = errorType;
    }

    @Override
    public ErrorType getErrorType() {
        return errorType;
    }
}
