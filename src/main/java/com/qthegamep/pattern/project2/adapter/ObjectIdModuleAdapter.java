package com.qthegamep.pattern.project2.adapter;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;

public class ObjectIdModuleAdapter {

    public SimpleModule buildModule() {
        return new SimpleModule()
                .addSerializer(ObjectId.class, new ObjectIdJsonSerializer())
                .addDeserializer(ObjectId.class, new ObjectIdJsonDeserializer());
    }
}
