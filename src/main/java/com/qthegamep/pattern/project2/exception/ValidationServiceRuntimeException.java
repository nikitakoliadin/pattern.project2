package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.ErrorType;

public class ValidationServiceRuntimeException extends GeneralServiceRuntimeException {

    public ValidationServiceRuntimeException(ErrorType errorType) {
        super(errorType);
    }
}
