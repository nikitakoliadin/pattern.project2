package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.Error;

public class XmlConverterRuntimeException extends GeneralServiceRuntimeException {

    public XmlConverterRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
