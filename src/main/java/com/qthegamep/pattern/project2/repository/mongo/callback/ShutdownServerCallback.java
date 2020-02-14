package com.qthegamep.pattern.project2.repository.mongo.callback;

import com.mongodb.async.SingleResultCallback;
import com.qthegamep.pattern.project2.service.ExitManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ShutdownServerCallback implements SingleResultCallback<Void> {

    private static final Logger LOG = LoggerFactory.getLogger(ShutdownServerCallback.class);

    private ExitManagerService exitManagerService;

    @Inject
    public ShutdownServerCallback(ExitManagerService exitManagerService) {
        this.exitManagerService = exitManagerService;
    }

    @Override
    public void onResult(Void aVoid, Throwable throwable) {
        if (throwable != null) {
            LOG.error("ERROR", throwable);
            exitManagerService.exit(1);
        }
    }
}
