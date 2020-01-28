package com.qthegamep.pattern.project2.config;

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

    private final HttpServer[] httpServers;

    public ShutdownHookConfig(HttpServer... httpServers) {
        this.httpServers = httpServers;
    }

    @Override
    public void run() {
        LOG.warn("Shutting down servers...");
        try {
            List<GrizzlyFuture<HttpServer>> futures = Arrays.stream(httpServers)
                    .map(httpServer -> httpServer.shutdown(gracePeriod, gracePeriodTimeUnit))
                    .collect(Collectors.toList());
            LOG.info("Waiting for servers to shut down... Grace period is {} {}", gracePeriod, gracePeriodTimeUnit);
            for (GrizzlyFuture<HttpServer> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            LOG.error("Error while shutting down servers", e);
        }
        LOG.info("Servers stopped!");
    }
}
