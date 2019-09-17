package com.qthegamep.pattern.project2.config;

import com.qthegamep.pattern.project2.exception.ApplicationConfigException;
import com.qthegamep.pattern.project2.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Optional;
import java.util.Properties;

public class ApplicationConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationConfig.class);

    public void init() throws ApplicationConfigException {
        try {
            loadServerIp();
            String configPath = System.getProperty("config.properties");
            if (configPath == null) {
                LOG.info("Load default config properties!");
                loadDefaultProperties();
            } else {
                LOG.info("Load custom config properties! config.properties: {}", configPath);
                loadCustomProperties(configPath);
            }
        } catch (Exception e) {
            LOG.error("ERROR", e);
            throw new ApplicationConfigException(e);
        }
    }

    private void loadServerIp() throws SocketException {
        Optional<String> serverIpOptional = getServerIp();
        if (serverIpOptional.isPresent()) {
            String serverIp = serverIpOptional.get();
            LOG.info("Server IP: {}", serverIp);
            System.setProperty("serverIp", serverIp);
        } else {
            LOG.warn("Server IP is not defined!");
        }
    }

    private Optional<String> getServerIp() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            LOG.debug("Interface display name: {}", networkInterface.getDisplayName());
            if (networkInterface.isLoopback()
                    || !networkInterface.isUp()
                    || networkInterface.isVirtual()
                    || networkInterface.isPointToPoint()
                    || networkInterface.getDisplayName().contains("docker")) {
                continue;
            }
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                String hostAddress = inetAddress.getHostAddress();
                LOG.debug("Interface display name: {} host address: {}", networkInterface.getDisplayName(), hostAddress);
                if (Inet4Address.class == inetAddress.getClass()) {
                    return Optional.of(hostAddress);
                }
            }
        }
        return Optional.empty();
    }

    private void loadDefaultProperties() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = ApplicationConfig.class.getResourceAsStream(Constants.DEFAULT_CONFIG_PROPERTIES_PATH.getValue())) {
            properties.load(inputStream);
            loadProperties(properties);
        }
    }

    private void loadCustomProperties(String path) throws Exception {
        Properties properties = new Properties();
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8)) {
            properties.load(inputStreamReader);
            loadProperties(properties);
        }
    }

    private void loadProperties(Properties properties) {
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
