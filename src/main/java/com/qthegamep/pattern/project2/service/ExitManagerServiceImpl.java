package com.qthegamep.pattern.project2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExitManagerServiceImpl implements ExitManagerService {

    private static final Logger LOG = LoggerFactory.getLogger(ExitManagerServiceImpl.class);

    @Override
    public void exit(int exitCode) {
        LOG.warn("Exit with code: {}", exitCode);
        System.exit(exitCode);
    }
}
