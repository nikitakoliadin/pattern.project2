package com.qthegamep.pattern.project2.service.adapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.qthegamep.pattern.project2.util.Constants;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class IsoDateJsonDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.MONGO_UTC_DATE_PATTERN);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(Constants.GMT_TIMEZONE));
            JsonNode jsonNode = jsonParser.readValueAsTree();
            String isoDate = jsonNode.get(Constants.JSON_DATE_FIELD_NAME).asText();
            return simpleDateFormat.parse(isoDate);
        } catch (ParseException e) {
            return null;
        }
    }
}
