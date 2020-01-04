package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.ErrorType;

public class CryptoServiceException extends GeneralServiceException {

    public CryptoServiceException(ErrorType errorType) {
        super(errorType);
    }

    public CryptoServiceException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }
}
