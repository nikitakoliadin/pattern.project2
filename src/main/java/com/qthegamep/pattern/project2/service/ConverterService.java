package com.qthegamep.pattern.project2.service;

import java.io.InputStream;

public interface ConverterService {

    <T> T fromJson(String entity, Class<T> modelClass);

    String toJson(Object model);

    <T> T fromXml(String entity, Class<T> modelClass);

    String toXml(Object model);

    String toString(InputStream inputStream);
}
