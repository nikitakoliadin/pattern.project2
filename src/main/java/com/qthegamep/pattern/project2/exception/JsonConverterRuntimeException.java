package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.ErrorType;

public class JsonConverterRuntimeException extends GeneralServiceRuntimeException {

    public JsonConverterRuntimeException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }
}
