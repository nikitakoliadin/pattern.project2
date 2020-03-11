package com.qthegamep.pattern.project2.model.container;

public enum RedisConnection {

    POOL_REDIS_CONNECTION("pool"),
    MASTER_SLAVE_REDIS_CONNECTION("master-slave"),
    CLUSTER_REDIS_CONNECTION("cluster");

    private String type;

    RedisConnection(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
