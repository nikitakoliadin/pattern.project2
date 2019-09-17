package com.qthegamep.pattern.project2.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface ConverterService {

    <T> T fromJson(String entity, Class<T> modelClass) throws IOException;

    String toJson(Object model) throws JsonProcessingException;
}
