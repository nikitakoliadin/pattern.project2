package com.qthegamep.pattern.project2.controller;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Singleton
@Path("/prometheus")
public class PrometheusMetricsControllerImpl implements PrometheusMetricsController {

    private PrometheusMeterRegistry prometheusMeterRegistry;

    @Inject
    public PrometheusMetricsControllerImpl(PrometheusMeterRegistry prometheusMeterRegistry) {
        this.prometheusMeterRegistry = prometheusMeterRegistry;
    }

    @Override
    @GET
    @Path("/metrics")
    @Produces(TextFormat.CONTENT_TYPE_004)
    public Response metrics() {
        return Response.status(Response.Status.OK)
                .entity(prometheusMeterRegistry.scrape())
                .build();
    }
}
