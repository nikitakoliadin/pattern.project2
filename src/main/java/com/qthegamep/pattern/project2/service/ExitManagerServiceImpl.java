package com.qthegamep.pattern.project2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ExitManagerServiceImpl implements ExitManagerService {

    private static final Logger LOG = LoggerFactory.getLogger(ExitManagerServiceImpl.class);

    private Runtime runtime;

    @Inject
    public ExitManagerServiceImpl(Runtime runtime) {
        this.runtime = runtime;
    }

    @Override
    public void exit(int exitCode) {
        LOG.warn("Exit with code: {}", exitCode);
        runtime.exit(exitCode);
    }
}
