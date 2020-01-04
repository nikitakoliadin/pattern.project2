package com.qthegamep.pattern.project2.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.StatusPrinter;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class LogConfig {

    public void configureLogLevels() {
        configureRootLogger();
    }

    private void configureRootLogger() {
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        String rootLogLevel = System.getProperty("root.logger.level");
        logger.setLevel(Level.toLevel(rootLogLevel));
        boolean rootLoggerFileEnabled = Boolean.parseBoolean(System.getProperty("root.logger.file.enable"));
        String dockerImageName = System.getProperty(Constants.DOCKER_IMAGE_NAME_PROPERTY);
        if (rootLoggerFileEnabled && dockerImageName != null && !dockerImageName.isEmpty()) {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            logger.addAppender(buildRollingFileAppender(loggerContext, dockerImageName));
            StatusPrinter.print(loggerContext);
            logger.warn("Log: {} Level: {} File appender enabled!", logger.getName(), logger.getLevel());
        } else {
            logger.warn("Log: {} Level: {} File appender not enabled!", logger.getName(), logger.getLevel());
        }
    }

    private RollingFileAppender<ILoggingEvent> buildRollingFileAppender(LoggerContext loggerContext, String dockerImageName) {
        String rootLoggerFileName = System.getProperty("root.logger.file.name");
        String rootLoggerFilePath = System.getProperty("root.logger.file.path");
        String rootLoggerFileAppender = System.getProperty("root.logger.file.appender");
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setContext(loggerContext);
        rollingFileAppender.setName(rootLoggerFileName);
        rollingFileAppender.setFile(rootLoggerFilePath.replaceAll(Constants.LOGGER_REPLACE_PATTERN, dockerImageName));
        rollingFileAppender.setAppend(Boolean.parseBoolean(rootLoggerFileAppender));
        rollingFileAppender.setRollingPolicy(buildTimeBasedRollingPolicy(loggerContext, rollingFileAppender, dockerImageName));
        rollingFileAppender.setEncoder(buildPatternLayoutEncoder(loggerContext, rollingFileAppender));
        rollingFileAppender.start();
        return rollingFileAppender;
    }

    private TimeBasedRollingPolicy<ILoggingEvent> buildTimeBasedRollingPolicy(LoggerContext loggerContext, RollingFileAppender<ILoggingEvent> rollingFileAppender, String dockerImageName) {
        String rootLoggerFileNamePattern = System.getProperty("root.logger.file.name.pattern");
        String rootLoggerFileMaxHistory = System.getProperty("root.logger.file.max.history");
        String rootLoggerFileTotalSizeCap = System.getProperty("root.logger.file.total.size.cap");
        TimeBasedRollingPolicy<ILoggingEvent> timeBasedRollingPolicy = new TimeBasedRollingPolicy<>();
        timeBasedRollingPolicy.setContext(loggerContext);
        timeBasedRollingPolicy.setParent(rollingFileAppender);
        timeBasedRollingPolicy.setFileNamePattern(rootLoggerFileNamePattern.replaceAll(Constants.LOGGER_REPLACE_PATTERN, dockerImageName));
        timeBasedRollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(buildSizeAndTimeBasedFNATP(loggerContext, timeBasedRollingPolicy));
        timeBasedRollingPolicy.setMaxHistory(Integer.parseInt(rootLoggerFileMaxHistory));
        timeBasedRollingPolicy.setTotalSizeCap(FileSize.valueOf(rootLoggerFileTotalSizeCap));
        timeBasedRollingPolicy.start();
        return timeBasedRollingPolicy;
    }

    private SizeAndTimeBasedFNATP<ILoggingEvent> buildSizeAndTimeBasedFNATP(LoggerContext loggerContext, TimeBasedRollingPolicy<ILoggingEvent> timeBasedRollingPolicy) {
        String rootLoggerFileMaxFileSize = System.getProperty("root.logger.file.max.file.size");
        SizeAndTimeBasedFNATP<ILoggingEvent> sizeAndTimeBasedFNATP = new SizeAndTimeBasedFNATP<>();
        sizeAndTimeBasedFNATP.setContext(loggerContext);
        sizeAndTimeBasedFNATP.setTimeBasedRollingPolicy(timeBasedRollingPolicy);
        sizeAndTimeBasedFNATP.setMaxFileSize(FileSize.valueOf(rootLoggerFileMaxFileSize));
        return sizeAndTimeBasedFNATP;
    }

    private PatternLayoutEncoder buildPatternLayoutEncoder(LoggerContext loggerContext, RollingFileAppender<ILoggingEvent> rollingFileAppender) {
        String rootLoggerFilePattern = System.getProperty("root.logger.file.pattern");
        PatternLayoutEncoder patternLayoutEncoder = new PatternLayoutEncoder();
        patternLayoutEncoder.setContext(loggerContext);
        patternLayoutEncoder.setParent(rollingFileAppender);
        patternLayoutEncoder.setCharset(StandardCharsets.UTF_8);
        patternLayoutEncoder.setPattern(rootLoggerFilePattern);
        patternLayoutEncoder.start();
        return patternLayoutEncoder;
    }
}
