package com.qthegamep.pattern.project2.util;

public enum Constants {

    DEFAULT_CONFIG_PROPERTIES_PATH("/config.properties"),
    MONGO_UTC_DATE_FORMAT("yyyy-MM-dd'T'HH:mm:ss.SSS"),
    JSON_DATE_FIELD_NAME("$date");

    private String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
