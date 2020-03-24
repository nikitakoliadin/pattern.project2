package com.qthegamep.pattern.project2.model.container;

public enum Error {

    PAGE_NOT_FOUND_ERROR(404),
    INTERNAL_ERROR(500),
    OPEN_API_ERROR(501),
    UNKNOWN_ERROR(502),
    JSON_CONVERTER_ERROR(503),
    XML_CONVERTER_ERROR(504),
    MONGO_SYNC_SAVE_ERROR_ERROR(510),
    MONGO_ASYNC_SAVE_ERROR_ERROR(511),
    REDIS_SAVE_ERROR(513),
    REDIS_SAVE_ALL_ERROR(514),
    REDIS_READ_ERROR(515),
    REDIS_READ_ALL_ERROR(516),
    ENCODE_HASH_ALGORITHM_NOT_EXISTS_ERROR(517),
    ENCODE_HASH_ERROR(518),
    INVALID_REQUEST_RESPONSE_ERROR(519),
    VALIDATION_ERROR(520),
    RETRY_ERROR(521),
    REDIS_REMOVE_ERROR(522),
    STRING_CONVERTER_ERROR(523);

    private int errorCode;

    Error(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
