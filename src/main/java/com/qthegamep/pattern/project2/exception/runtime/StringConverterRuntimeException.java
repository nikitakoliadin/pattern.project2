package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class StringConverterRuntimeException extends GeneralServiceRuntimeException {

    public StringConverterRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
