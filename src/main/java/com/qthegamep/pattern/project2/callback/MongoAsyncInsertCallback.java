package com.qthegamep.pattern.project2.callback;

import com.mongodb.async.SingleResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoAsyncInsertCallback implements SingleResultCallback<Void> {

    private static final Logger LOG = LoggerFactory.getLogger(MongoAsyncInsertCallback.class);

    private String requestId;

    public MongoAsyncInsertCallback(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public void onResult(Void result, Throwable throwable) {
        if (throwable == null) {
            LOG.debug("Inserted! RequestId: {}", requestId);
        } else {
            LOG.error("ERROR! RequestId: {}", requestId, throwable);
        }
    }
}
