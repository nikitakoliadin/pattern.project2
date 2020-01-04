package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class JsonConverterRuntimeException extends GeneralServiceRuntimeException {

    public JsonConverterRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
