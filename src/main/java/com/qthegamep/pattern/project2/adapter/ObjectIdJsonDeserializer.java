package com.qthegamep.pattern.project2.adapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.qthegamep.pattern.project2.util.Constants;
import org.bson.types.ObjectId;

class ObjectIdJsonDeserializer extends JsonDeserializer<ObjectId> {

    @Override
    public ObjectId deserialize(JsonParser jsonParser, DeserializationContext context) {
        try {
            JsonNode jsonNode = jsonParser.readValueAsTree();
            String id = jsonNode.get(Constants.JSON_OBJECT_ID_FIELD_NAME).asText();
            return new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
    }
}
