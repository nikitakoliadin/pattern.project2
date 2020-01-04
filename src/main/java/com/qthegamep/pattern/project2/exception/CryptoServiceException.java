package com.qthegamep.pattern.project2.exception;

import com.qthegamep.pattern.project2.model.container.Error;

public class CryptoServiceException extends GeneralServiceException {

    public CryptoServiceException(Error error) {
        super(error);
    }

    public CryptoServiceException(Throwable cause, Error error) {
        super(cause, error);
    }
}
