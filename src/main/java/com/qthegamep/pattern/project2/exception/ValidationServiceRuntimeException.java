package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.Error;

public class ValidationServiceRuntimeException extends GeneralServiceRuntimeException {

    public ValidationServiceRuntimeException(String message, Error error) {
        super(message, error);
    }
}
