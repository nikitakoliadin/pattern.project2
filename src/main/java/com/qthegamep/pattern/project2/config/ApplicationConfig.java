package com.qthegamep.pattern.project2.config;

import com.qthegamep.pattern.project2.exception.ApplicationConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ApplicationConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);

    private ApplicationConfig() {
    }

    public static void init() throws ApplicationConfigException {
        try {
            String configPath = System.getProperty("config.properties");
            if (configPath == null) {
                LOG.info("Load default config properties!");
                loadDefaultProperties();
            } else {
                LOG.info("Load config properties! config.properties: {}", configPath);
                loadProperties(configPath);
            }
        } catch (Exception e) {
            LOG.error("ERROR", e);
            throw new ApplicationConfigException(e);
        }
    }

    private static void loadProperties(String path) throws Exception {
        Properties properties = new Properties();
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8)) {
            properties.load(inputStreamReader);
            load(properties);
        }
    }

    private static void loadDefaultProperties() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = ApplicationConfig.class.getResourceAsStream("/config.properties")) {
            properties.load(inputStream);
            load(properties);
        }
    }

    private static void load(Properties properties) {
        properties.stringPropertyNames()
                .stream()
                .sorted()
                .forEach(key -> {
                    String value = (String) properties.get(key);
                    LOG.info("SYSTEM PROPERTY: {}={}", key, value);
                    System.setProperty(key, value);
                });
    }
}
