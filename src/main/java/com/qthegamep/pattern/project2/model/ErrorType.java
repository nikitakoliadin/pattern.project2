package com.qthegamep.pattern.project2.model;

public enum ErrorType {

    PAGE_NOT_FOUND_ERROR(404),
    INTERNAL_ERROR(500),
    OPEN_API_ERROR(501),
    UNKNOWN_ERROR(502),
    JSON_CONVERTER_ERROR(503),
    XML_CONVERTER_ERROR(504);

    private int errorCode;

    ErrorType(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
