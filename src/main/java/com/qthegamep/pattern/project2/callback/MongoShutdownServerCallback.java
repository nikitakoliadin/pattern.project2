package com.qthegamep.pattern.project2.callback;

import com.mongodb.async.SingleResultCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoShutdownServerCallback implements SingleResultCallback<Void> {

    private static final Logger LOG = LoggerFactory.getLogger(MongoShutdownServerCallback.class);

    @Override
    public void onResult(Void aVoid, Throwable throwable) {
        if (throwable != null) {
            LOG.error("ERROR", throwable);
            System.exit(1);
        }
    }
}
