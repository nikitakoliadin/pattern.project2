package com.qthegamep.pattern.project2.util;

public class Constants {

    public static final String MONGO_UTC_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String GMT_TIMEZONE = "GMT";
    public static final String JSON_DATE_FIELD_NAME = "$date";
    public static final String START_TIME_HEADER = "startTime";
    public static final String DURATION_HEADER = "duration";
    public static final String REQUEST_ID_HEADER = "requestId";
    public static final String GRIZZLY = "grizzly";
    public static final String JSON_OBJECT_ID_FIELD_NAME = "$oid";
    public static final String STANDALONE_MONGO_DB_TYPE = "standalone";
    public static final String CLUSTER_MONGO_DB_TYPE = "cluster";
    public static final String JSON_OBJECT_ID_KEY = "_id";
    public static final String POOL_REDIS_TYPE = "pool";
    public static final String CLUSTER_REDIS_TYPE = "cluster";
    public static final String SERVER_IP_PROPERTY = "serverIp";
    public static final String DOCKER_IMAGE_NAME_PROPERTY = "docker.image.name";
    public static final String LOGGER_REPLACE_PATTERN = "\\{}";
    public static final String REDIS = "redis";

    private Constants() {
    }
}
