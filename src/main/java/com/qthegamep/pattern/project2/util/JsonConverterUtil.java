package com.qthegamep.pattern.project2.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qthegamep.pattern.project2.adapter.IsoDateJsonAdapter;

import java.util.Date;

public class JsonConverterUtil {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(Date.class, new IsoDateJsonAdapter())
            .create();

    public static <T> T fromJson(String entity, Class<T> modelClass) {
        return GSON.fromJson(entity, modelClass);
    }

    public static String toJson(Object model) {
        return GSON.toJson(model);
    }
}
