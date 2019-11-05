package com.qthegamep.pattern.project2;

import com.qthegamep.pattern.project2.binder.ApplicationBinder;
import com.qthegamep.pattern.project2.config.ApplicationConfig;
import com.qthegamep.pattern.project2.config.GrizzlyServersShutdownHook;
import com.qthegamep.pattern.project2.config.IOStrategyFactory;
import com.qthegamep.pattern.project2.probe.GrizzlyThreadPoolProbe;
import com.qthegamep.pattern.project2.probe.TaskQueueSizeProbe;
import com.qthegamep.pattern.project2.exception.ApplicationConfigInitializationException;
import com.qthegamep.pattern.project2.model.IoStrategyType;
import com.qthegamep.pattern.project2.util.Constants;
import com.qthegamep.pattern.project2.util.Paths;
import io.prometheus.client.exporter.MetricsServlet;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.monitoring.MonitoringConfig;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.grizzly.threadpool.ThreadPoolProbe;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws ApplicationConfigInitializationException, InterruptedException {
        initConfigs();
        String host = System.getProperty("application.host", "0.0.0.0");
        String port = System.getProperty("application.port", "8080");
        String applicationContext = System.getProperty("application.context", "");
        String applicationUrl = Constants.HTTP.getValue() + host + ":" + port + applicationContext;
        HttpServer applicationHttpServer = startServer(applicationUrl);
        String swaggerUrl = System.getProperty("application.swagger.url", "/docs");
        String swaggerPath = addSwaggerUIMapping(applicationHttpServer, applicationContext + swaggerUrl);
        String metricsPort = System.getProperty("application.metrics.port", "8081");
        String metricsContext = System.getProperty("application.metrics.context", "/metrics");
        String metricsUrl = Constants.HTTP.getValue() + host + ":" + metricsPort + metricsContext;
        HttpServer metricsHttpServer = startMetricsServer(metricsUrl);
        Runtime.getRuntime().addShutdownHook(new GrizzlyServersShutdownHook(applicationHttpServer, metricsHttpServer));
        LOG.info("{} application started at {}", Application.class.getPackage().getName(), applicationUrl);
        LOG.info("Swagger openApi available at {}", swaggerPath);
        LOG.info("Metrics started at {}", metricsUrl);
        Thread.currentThread().join();
    }

    private static void initConfigs() throws ApplicationConfigInitializationException {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.init();
    }

    private static HttpServer startServer(String applicationUrl) {
        URI applicationUri = URI.create(applicationUrl);
        ResourceConfig resourceConfig = new ResourceConfig()
                .packages(Application.class.getPackage().getName())
                .register(ApplicationBinder.builder().build());
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(applicationUri, resourceConfig, false);
        configServer(httpServer);
        return httpServer;
    }

    private static void configServer(HttpServer httpServer) {
        try {
            ServerConfiguration serverConfiguration = httpServer.getServerConfiguration();
            serverConfiguration.setJmxEnabled(true);
            TCPNIOTransport grizzlyTransport = httpServer.getListener(Constants.GRIZZLY.getValue()).getTransport();
            ThreadPoolConfig threadPoolConfig = ThreadPoolConfig.defaultConfig()
                    .setPoolName(Constants.APPLICATION_GRIZZLY_POOL_NAME.getValue())
                    .setCorePoolSize(Integer.parseInt(System.getProperty("server.core.pool.size")))
                    .setMaxPoolSize(Integer.parseInt(System.getProperty("server.max.pool.size")))
                    .setQueueLimit(Integer.parseInt(System.getProperty("server.queue.limit")));
            grizzlyTransport.configureBlocking(false);
            grizzlyTransport.setSelectorRunnersCount(Runtime.getRuntime().availableProcessors() * Integer.parseInt(System.getProperty("server.selector.runners.multiplier")));
            grizzlyTransport.setWorkerThreadPoolConfig(threadPoolConfig);
            grizzlyTransport.setKernelThreadPoolConfig(threadPoolConfig);
            grizzlyTransport.setIOStrategy(new IOStrategyFactory().createIOStrategy(IoStrategyType.DYNAMIC_IO_STRATEGY));
            addProbes(grizzlyTransport);
            httpServer.start();
            LOG.info("\nBlocking Transport(T/F): {}\nNum SelectorRunners: {}\nNum WorkerThreads: {}\nNum KernelThreadPool: {}\nQueue limit: {}",
                    grizzlyTransport.isBlocking(),
                    grizzlyTransport.getSelectorRunnersCount(),
                    grizzlyTransport.getWorkerThreadPoolConfig().getCorePoolSize(),
                    grizzlyTransport.getKernelThreadPoolConfig().getCorePoolSize(),
                    grizzlyTransport.getWorkerThreadPoolConfig().getQueueLimit());
        } catch (Exception e) {
            LOG.error("Error", e);
        }
    }

    private static void addProbes(TCPNIOTransport grizzlyTransport) {
        MonitoringConfig<ThreadPoolProbe> threadPoolMonitoringConfig = grizzlyTransport.getThreadPoolMonitoringConfig();
        threadPoolMonitoringConfig.addProbes(new TaskQueueSizeProbe(), new GrizzlyThreadPoolProbe());
        LOG.info("Probes was added");
    }

    private static String addSwaggerUIMapping(HttpServer httpServer, String contextPath) {
        ClassLoader classLoader = Application.class.getClassLoader();
        CLStaticHttpHandler docsStaticHttpHandler = new CLStaticHttpHandler(classLoader, "swagger-ui/");
        docsStaticHttpHandler.setFileCacheEnabled(false);
        String urlPattern = "/";
        HttpHandlerRegistration httpHandlerRegistration = HttpHandlerRegistration.builder()
                .contextPath(contextPath)
                .urlPattern(urlPattern)
                .build();
        ServerConfiguration serverConfiguration = httpServer.getServerConfiguration();
        serverConfiguration.addHttpHandler(docsStaticHttpHandler, httpHandlerRegistration);
        NetworkListener grizzlyListener = httpServer.getListener(Constants.GRIZZLY.getValue());
        return Constants.HTTP.getValue() + grizzlyListener.getHost() + ":" + grizzlyListener.getPort() + contextPath + urlPattern;
    }

    private static HttpServer startMetricsServer(String metricsUrl) {
        URI metricsUri = URI.create(metricsUrl);
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(metricsUri, false);
        configMetricsServer(httpServer);
        return httpServer;
    }

    private static void configMetricsServer(HttpServer httpServer) {
        try {
            TCPNIOTransport grizzlyTransport = httpServer.getListener(Constants.GRIZZLY.getValue()).getTransport();
            ThreadPoolConfig threadPoolConfig = ThreadPoolConfig.defaultConfig()
                    .setPoolName(Constants.PROMETHEUS_GRIZZLY_POOL_NAME.getValue())
                    .setCorePoolSize(Integer.parseInt(System.getProperty("server.core.pool.size")))
                    .setMaxPoolSize(Integer.parseInt(System.getProperty("server.max.pool.size")))
                    .setQueueLimit(Integer.parseInt(System.getProperty("server.queue.limit")));
            grizzlyTransport.setSelectorRunnersCount(Runtime.getRuntime().availableProcessors() * Integer.parseInt(System.getProperty("server.selector.runners.multiplier")));
            grizzlyTransport.setWorkerThreadPoolConfig(threadPoolConfig);
            grizzlyTransport.setKernelThreadPoolConfig(threadPoolConfig);
            grizzlyTransport.setIOStrategy(new IOStrategyFactory().createIOStrategy(IoStrategyType.DYNAMIC_IO_STRATEGY));
            WebappContext webappContext = new WebappContext("Prometheus metrics", Paths.METRICS_PATH);
            webappContext.addServlet("Prometheus metrics servlet", new MetricsServlet());
            webappContext.deploy(httpServer);
            httpServer.start();
            LOG.info("\nBlocking Transport(T/F): {}\nNum SelectorRunners: {}\nNum WorkerThreads: {}\nNum KernelThreadPool: {}\nQueue limit: {}",
                    grizzlyTransport.isBlocking(),
                    grizzlyTransport.getSelectorRunnersCount(),
                    grizzlyTransport.getWorkerThreadPoolConfig().getCorePoolSize(),
                    grizzlyTransport.getKernelThreadPoolConfig().getCorePoolSize(),
                    grizzlyTransport.getWorkerThreadPoolConfig().getQueueLimit());

        } catch (Exception e) {
            LOG.error("Error", e);
        }
    }
}
