package com.qthegamep.pattern.project2.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class LogConfig {

    public void configureLogLevels() {
        configureRootLogger();
    }

    private void configureRootLogger() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        String rootLogLevel = System.getProperty("root.logger.level");
        logger.setLevel(Level.toLevel(rootLogLevel));
        logger.warn("Log: {} Level: {}", logger.getName(), logger.getLevel());
    }
}
