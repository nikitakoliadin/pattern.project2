package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.ErrorType;

public class XmlConverterRuntimeException extends GeneralServiceRuntimeException {

    public XmlConverterRuntimeException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }
}
