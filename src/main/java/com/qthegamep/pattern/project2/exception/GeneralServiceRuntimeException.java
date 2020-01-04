package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.ErrorType;

abstract class GeneralServiceRuntimeException extends RuntimeException implements ServiceException {

    private final ErrorType errorType;

    GeneralServiceRuntimeException(ErrorType errorType) {
        this.errorType = errorType;
    }

    GeneralServiceRuntimeException(Throwable cause, ErrorType errorType) {
        super(cause);
        this.errorType = errorType;
    }

    public GeneralServiceRuntimeException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    @Override
    public ErrorType getErrorType() {
        return errorType;
    }
}
