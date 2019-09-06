package com.qthegamep.pattern.project2.util;

public enum Constants {

    MONGO_UTC_DATE_FORMAT("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
