package com.qthegamep.pattern.project2.controller;

import io.swagger.v3.jaxrs2.integration.resources.BaseOpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.net.URI;

@Singleton
@Path("openapi.{type:json|yaml}")
@OpenAPIDefinition(
        info = @Info(title = "Pattern Project 2"),
        security = {@SecurityRequirement(name = "Authorization")})
@SecurityScheme(
        name = "Authorization",
        description = "Bearer sid",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization")
public class OpenApiControllerImpl extends BaseOpenApiResource {

    @Context
    private ServletConfig servletConfig;

    @Context
    private Application application;

    public OpenApiControllerImpl() {
        openApiConfiguration = new SwaggerConfiguration()
                .openAPI(new OpenAPI())
                .prettyPrint(true);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, "application/yaml"})
    @Operation(hidden = true)
    public Response getOpenApi(@Context HttpHeaders headers,
                               @Context UriInfo uriInfo,
                               @PathParam("type") String type) throws Exception {
        URI baseUri = uriInfo.getBaseUri();
        String applicationUrl = baseUri.toString();
        if (baseUri.getPort() == 80) {
            applicationUrl = applicationUrl.replaceFirst(":80", "");
        }
        openApiConfiguration.getOpenAPI().addServersItem(new Server().url(applicationUrl));
        return super.getOpenApi(headers, servletConfig, application, uriInfo, type);
    }
}
