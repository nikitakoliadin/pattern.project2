package com.qthegamep.pattern.project2.exception.runtime;

import com.qthegamep.pattern.project2.model.container.Error;

public class XmlConverterServiceRuntimeException extends GeneralServiceRuntimeException {

    public XmlConverterServiceRuntimeException(Throwable cause, Error error) {
        super(cause, error);
    }
}
