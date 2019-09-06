package com.qthegamep.pattern.project2.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.qthegamep.pattern.project2.adapter.IsoDateModuleAdapter;

import java.io.IOException;

public class JsonConverterUtil {

    private static final ObjectMapper JSON = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
            .registerModule(new IsoDateModuleAdapter().buildModule());

    public static <T> T fromJson(String entity, Class<T> modelClass) throws IOException {
        return JSON.readValue(entity, modelClass);
    }

    public static String toJson(Object model) throws JsonProcessingException {
        return JSON.writeValueAsString(model);
    }
}
