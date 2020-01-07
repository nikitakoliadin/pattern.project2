package com.qthegamep.pattern.project2.service.adapter;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;

public class ObjectIdJsonModuleAdapter {

    public SimpleModule buildModule() {
        return new SimpleModule()
                .addSerializer(ObjectId.class, new ObjectIdJsonSerializer())
                .addDeserializer(ObjectId.class, new ObjectIdJsonDeserializer());
    }
}
