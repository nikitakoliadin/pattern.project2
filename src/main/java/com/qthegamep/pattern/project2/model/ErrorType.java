package com.qthegamep.pattern.project2.model;

public enum ErrorType {

    PAGE_NOT_FOUND_ERROR(404),
    INTERNAL_ERROR(500),
    OPEN_API_ERROR(501),
    UNKNOWN_ERROR(502),
    JSON_CONVERTER_ERROR(503),
    XML_CONVERTER_ERROR(504),
    MONGO_DB_NOT_EXISTING_TYPE_ERROR(505),
    SYNC_MONGO_DB_CONNECTOR_ERROR(506),
    ASYNC_MONGO_DB_CONNECTOR_ERROR(507),
    REDIS_POOL_CONNECTOR_ERROR(508),
    REDIS_CLUSTER_CONNECTOR_ERROR(509),
    CLOSE_CLUSTER_REDIS_ERROR(510),
    MONGO_SYNC_SAVE_ERROR_ERROR(511),
    MONGO_ASYNC_SAVE_ERROR_ERROR(512),
    REDIS_NOT_EXISTING_TYPE_ERROR(513),
    REDIS_SAVE_ERROR(514);

    private int errorCode;

    ErrorType(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
