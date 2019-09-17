package com.qthegamep.pattern.project2.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.qthegamep.pattern.project2.adapter.IsoDateModuleAdapter;

import java.io.IOException;

public class ConverterServiceImpl implements ConverterService {

    private final ObjectMapper JSON = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
            .registerModule(new IsoDateModuleAdapter().buildModule());

    @Override
    public <T> T fromJson(String entity, Class<T> modelClass) throws IOException {
        return JSON.readValue(entity, modelClass);
    }

    @Override
    public String toJson(Object model) throws JsonProcessingException {
        return JSON.writeValueAsString(model);
    }
}
