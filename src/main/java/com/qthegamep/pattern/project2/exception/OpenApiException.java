package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.Error;

public class OpenApiException extends GeneralServiceException {

    public OpenApiException(Throwable cause, Error error) {
        super(cause, error);
    }
}
