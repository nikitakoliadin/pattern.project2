package com.qthegamep.pattern.project2.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.qthegamep.pattern.project2.util.Constants;
import org.bson.types.ObjectId;

import java.io.IOException;

class ObjectIdJsonSerializer extends JsonSerializer<ObjectId> {

    @Override
    public void serialize(ObjectId objectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject(Constants.JSON_OBJECT_ID_FIELD_NAME);
        jsonGenerator.writeObjectField(Constants.JSON_OBJECT_ID_FIELD_NAME, objectId.toHexString());
        jsonGenerator.writeEndObject();
    }
}
