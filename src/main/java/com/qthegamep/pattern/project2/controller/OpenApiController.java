package com.qthegamep.pattern.project2.controller;

import com.qthegamep.pattern.project2.model.dto.ErrorResponse;
import com.qthegamep.pattern.project2.exception.OpenApiException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

@Tag(name = "Swagger Open API")
@OpenAPIDefinition(
        info = @Info(title = "Cloud Api Back"),
        security = {@SecurityRequirement(name = "Authorization")})
@SecurityScheme(
        name = "Authorization",
        description = "Use 'Bearer sid' token for authorisation. Bearer test",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization")
@Singleton
public interface OpenApiController {

    @Operation(
            summary = "Get Open API documentation",
            description = "Return Swagger Open API documentation",
            hidden = true)
    @ApiResponse(
            responseCode = "200",
            description = "Return Open API successfully")
    @ApiResponse(
            responseCode = "others",
            description = "Error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GET
    @Path("/openapi.{type:json|yaml}")
    @Produces({MediaType.APPLICATION_JSON, "application/yaml"})
    Response getOpenApi(@Context HttpHeaders headers,
                        @Context UriInfo uriInfo,
                        @PathParam("type") String type) throws OpenApiException;
}
