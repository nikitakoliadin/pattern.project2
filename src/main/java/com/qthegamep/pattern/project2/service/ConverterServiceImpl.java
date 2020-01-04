package com.qthegamep.pattern.project2.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.qthegamep.pattern.project2.adapter.IsoDateModuleAdapter;
import com.qthegamep.pattern.project2.adapter.ObjectIdModuleAdapter;
import com.qthegamep.pattern.project2.exception.runtime.JsonConverterRuntimeException;
import com.qthegamep.pattern.project2.exception.runtime.XmlConverterRuntimeException;
import com.qthegamep.pattern.project2.model.container.Error;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ConverterServiceImpl implements ConverterService {

    private final ObjectMapper json = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
            .registerModule(new IsoDateModuleAdapter().buildModule())
            .registerModule(new ObjectIdModuleAdapter().buildModule());
    private final XmlMapper xml = new XmlMapper();

    @Override
    public <T> T fromJson(String entity, Class<T> modelClass) {
        try {
            return json.readValue(entity, modelClass);
        } catch (IOException e) {
            throw new JsonConverterRuntimeException(e, Error.JSON_CONVERTER_ERROR);
        }
    }

    @Override
    public String toJson(Object model) {
        try {
            return json.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new JsonConverterRuntimeException(e, Error.JSON_CONVERTER_ERROR);
        }
    }

    @Override
    public <T> T fromXml(String entity, Class<T> modelClass) {
        try {
            return xml.readValue(entity, modelClass);
        } catch (IOException e) {
            throw new XmlConverterRuntimeException(e, Error.XML_CONVERTER_ERROR);
        }
    }

    @Override
    public String toXml(Object model) {
        try {
            return xml.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new XmlConverterRuntimeException(e, Error.XML_CONVERTER_ERROR);
        }
    }

    @Override
    public String toString(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .parallel()
                .collect(Collectors.joining("\n"));
    }
}
