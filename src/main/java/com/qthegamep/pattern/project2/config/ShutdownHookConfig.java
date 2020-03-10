package com.qthegamep.pattern.project2.config;

import com.qthegamep.pattern.project2.binder.application.ApplicationBinder;
import org.glassfish.grizzly.GrizzlyFuture;
import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ShutdownHookConfig extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(ShutdownHookConfig.class);

    private int gracePeriod = Integer.parseInt(System.getProperty("shutdown.hook.grace.period"));
    private TimeUnit gracePeriodTimeUnit = TimeUnit.SECONDS;

    private ApplicationBinder applicationBinder;
    private HttpServer mainServer;
    private final HttpServer[] otherServers;

    public ShutdownHookConfig(ApplicationBinder applicationBinder,
                              HttpServer mainServer,
                              HttpServer... otherServers) {
        this.applicationBinder = applicationBinder;
        this.mainServer = mainServer;
        this.otherServers = otherServers;
    }

    @Override
    public void run() {
        shutdownMainServer();
        shutdownDatabaseConnections();
        shutdownOtherServers();
    }

    private void shutdownMainServer() {
        LOG.warn("Shutting down main server...");
        try {
            GrizzlyFuture<HttpServer> futureShutdown = mainServer.shutdown(gracePeriod, gracePeriodTimeUnit);
            LOG.info("Waiting for main server to shut down... Grace period is {} {}", gracePeriod, gracePeriodTimeUnit);
            futureShutdown.get();
        } catch (Exception e) {
            LOG.error("Error while shutting down main server", e);
        }
        LOG.info("Main server stopped!");
    }

    private void shutdownDatabaseConnections() {
        LOG.warn("Shutting down database connections");
        try {
            applicationBinder.getDatabaseConnectorService().closeAll();
        } catch (Exception e) {
            LOG.error("Error while shutting down database connections");
        }
        LOG.info("Database connections closed!");
    }

    private void shutdownOtherServers() {
        LOG.warn("Shutting down other servers...");
        try {
            List<GrizzlyFuture<HttpServer>> futureShutdowns = Arrays.stream(otherServers)
                    .map(httpServer -> httpServer.shutdown(gracePeriod, gracePeriodTimeUnit))
                    .collect(Collectors.toList());
            LOG.info("Waiting for other servers to shut down... Grace period is {} {}", gracePeriod, gracePeriodTimeUnit);
            for (GrizzlyFuture<HttpServer> futureShutdown : futureShutdowns) {
                futureShutdown.get();
            }
        } catch (Exception e) {
            LOG.error("Error while shutting down other servers", e);
        }
        LOG.info("Other servers stopped!");
    }
}
