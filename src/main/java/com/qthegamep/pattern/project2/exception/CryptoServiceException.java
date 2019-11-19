package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.ErrorType;

public class CryptoServiceException extends GeneralServiceException {

    public CryptoServiceException(Throwable cause, ErrorType errorType) {
        super(cause, errorType);
    }
}
