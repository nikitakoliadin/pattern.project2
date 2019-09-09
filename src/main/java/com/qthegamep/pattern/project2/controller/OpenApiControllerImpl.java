package com.qthegamep.pattern.project2.controller;

import io.swagger.v3.jaxrs2.integration.resources.BaseOpenApiResource;
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
@Path("/swagger")
public class OpenApiControllerImpl extends BaseOpenApiResource implements OpenApiController {

    @Context
    private ServletConfig servletConfig;

    @Context
    private Application application;

    public OpenApiControllerImpl() {
        openApiConfiguration = new SwaggerConfiguration()
                .openAPI(new OpenAPI())
                .prettyPrint(true);
    }

    @Override
    @GET
    @Path("/openapi.{type:json|yaml}")
    @Produces({MediaType.APPLICATION_JSON, "application/yaml"})
    public Response getOpenApi(@Context HttpHeaders headers,
                               @Context UriInfo uriInfo,
                               @PathParam("type") String type) throws Exception {
        URI baseUri = uriInfo.getBaseUri();
        String applicationUrl = baseUri.toString();
        if (baseUri.getPort() == 80) {
            applicationUrl = applicationUrl.replaceFirst(":80", "");
        }
        Server server = new Server().url(applicationUrl);
        openApiConfiguration.getOpenAPI().addServersItem(server);
        return super.getOpenApi(headers, servletConfig, application, uriInfo, type);
    }
}
