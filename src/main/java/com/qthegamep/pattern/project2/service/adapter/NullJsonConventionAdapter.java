package com.qthegamep.pattern.project2.service.adapter;

import org.bson.codecs.pojo.ClassModelBuilder;
import org.bson.codecs.pojo.Convention;
import org.bson.codecs.pojo.PropertyModelBuilder;

public class NullJsonConventionAdapter implements Convention {

    private static final String OBJECT_ID_WRITE_FIELD_NAME = "objectId";

    @Override
    public void apply(ClassModelBuilder<?> classModelBuilder) {
        for (PropertyModelBuilder<?> propertyModelBuilder : classModelBuilder.getPropertyModelBuilders()) {
            if (!propertyModelBuilder.getWriteName().equalsIgnoreCase(OBJECT_ID_WRITE_FIELD_NAME)) {
                propertyModelBuilder.propertySerialization(value -> true);
            }
        }
    }
}
