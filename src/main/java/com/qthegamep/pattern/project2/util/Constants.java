package com.qthegamep.pattern.project2.util;

public enum Constants {

    DEFAULT_CONFIG_PROPERTIES_PATH("/config.properties"),
    MONGO_UTC_DATE_FORMAT("yyyy-MM-dd'T'HH:mm:ss.SSS"),
    JSON_DATE_FIELD_NAME("$date"),
    START_TIME_HEADER("startTime"),
    DURATION_HEADER("duration"),
    X_REQUEST_ID_HEADER("x-request-id"),
    REQUEST_ID_HEADER("requestId"),
    X_FORWARDED_FOR_HEADER("X-Forwarded-For"),
    DEFAULT_LANGUAGE("uk"),
    ERROR_MESSAGES_LOCALIZATION("localization.error_messages");

    private String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
