package com.qthegamep.pattern.project2.controller;

import com.qthegamep.pattern.project2.dto.ErrorResponseDTO;
import io.prometheus.client.exporter.common.TextFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Tag(name = "Prometheus API")
@FunctionalInterface
public interface PrometheusMetricsController {

    @Operation(
            summary = "Get metrics",
            description = "Metrics that contains class loader metrics, JVM memory metrics, JVM GC metrics, processor metrics and JVM thread metrics")
    @ApiResponse(
            responseCode = "200",
            description = "Return metrics")
    @ApiResponse(
            responseCode = "others",
            description = "Error",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @GET
    @Path("/metrics")
    @Produces(TextFormat.CONTENT_TYPE_004)
    Response metrics();
}
