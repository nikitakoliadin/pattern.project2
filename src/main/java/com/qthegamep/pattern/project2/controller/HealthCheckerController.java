package com.qthegamep.pattern.project2.controller;

import com.qthegamep.pattern.project2.dto.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

@Tag(name = "Health Checker API")
@Singleton
public interface HealthCheckerController {

    @Operation(
            summary = "Test service status",
            description = "Check if service is alive")
    @ApiResponse(
            responseCode = "200",
            description = "Service is alive")
    @ApiResponse(
            responseCode = "others",
            description = "Service is dead",
            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    @GET
    Response test();
}
