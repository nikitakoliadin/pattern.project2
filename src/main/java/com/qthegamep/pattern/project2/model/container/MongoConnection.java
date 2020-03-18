package com.qthegamep.pattern.project2.model.container;

public enum MongoConnection {

    STANDALONE_MONGO_CONNECTION("standalone"),
    CLUSTER_MONGO_CONNECTION("cluster");

    private String type;

    MongoConnection(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
