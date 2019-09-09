package com.qthegamep.pattern.project2.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

@FunctionalInterface
@Singleton
@OpenAPIDefinition(
        info = @Info(title = "Pattern Project 2"),
        security = {@SecurityRequirement(name = "Authorization")})
@SecurityScheme(
        name = "Authorization",
        description = "Bearer sid",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization")
public interface OpenApiController {

    @GET
    @Path("/openapi.{type:json|yaml}")
    @Produces({MediaType.APPLICATION_JSON, "application/yaml"})
    @Operation(hidden = true)
    Response getOpenApi(@Context HttpHeaders headers,
                        @Context UriInfo uriInfo,
                        @PathParam("type") String type) throws Exception;
}
