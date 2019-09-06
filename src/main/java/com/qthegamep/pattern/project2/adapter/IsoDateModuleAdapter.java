package com.qthegamep.pattern.project2.adapter;

import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Date;

public class IsoDateModuleAdapter {

    public SimpleModule buildModule() {
        return new SimpleModule()
                .addSerializer(Date.class, new IsoDateJsonSerializer())
                .addDeserializer(Date.class, new IsoDateJsonDeserializer());
    }
}
