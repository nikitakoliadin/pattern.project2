package com.qthegamep.pattern.project2.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.qthegamep.pattern.project2.util.Constants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class IsoDateJsonSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.MONGO_UTC_DATE_FORMAT.getValue());
        String isoDate = simpleDateFormat.format(date);
        jsonGenerator.writeStartObject("$date");
        jsonGenerator.writeObjectField("$date", isoDate);
    }
}
