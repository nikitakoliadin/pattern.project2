package com.qthegamep.pattern.project2.adapter;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class IsoDateJsonAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private static final Logger LOG = LoggerFactory.getLogger(IsoDateJsonAdapter.class);
    private static final String MONGO_UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        if (Objects.isNull(date)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MONGO_UTC_DATE_FORMAT);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("$date", simpleDateFormat.format(date));
        return jsonObject;
    }

    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MONGO_UTC_DATE_FORMAT);
            String date = jsonElement.getAsJsonObject().get("$date").getAsString();
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            LOG.error("ERROR: {}", e.getMessage(), e);
            return null;
        }
    }
}
