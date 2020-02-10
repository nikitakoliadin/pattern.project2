package com.qthegamep.pattern.project2.controller;

import com.qthegamep.pattern.project2.exception.compile.OpenApiControllerException;
import com.qthegamep.pattern.project2.model.container.Error;
import com.qthegamep.pattern.project2.util.Paths;
import io.swagger.v3.jaxrs2.integration.resources.BaseOpenApiResource;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.models.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.net.URI;

@Singleton
@Path(Paths.SWAGGER_PATH)
public class OpenApiControllerImpl extends BaseOpenApiResource implements OpenApiController {

    private static final Logger LOG = LoggerFactory.getLogger(OpenApiControllerImpl.class);

    private ServletConfig servletConfig;
    private Application application;

    @Inject
    public OpenApiControllerImpl(@Context ServletConfig servletConfig,
                                 @Context Application application,
                                 OpenAPIConfiguration openAPIConfiguration) {
        this.servletConfig = servletConfig;
        this.application = application;
        this.openApiConfiguration = openAPIConfiguration;
    }

    @Override
    @GET
    @Path("/openapi.{type:json|yaml}")
    @Produces({MediaType.APPLICATION_JSON, "application/yaml"})
    public Response getOpenApi(@Context HttpHeaders headers,
                               @Context UriInfo uriInfo,
                               @PathParam("type") String type) throws OpenApiControllerException {
        try {
            URI baseUri = uriInfo.getBaseUri();
            String applicationUrl = baseUri.getPath();
            LOG.debug("Application url: {}", applicationUrl);
            Server httpServer = new Server().url(applicationUrl);
            openApiConfiguration.getOpenAPI().addServersItem(httpServer);
            return super.getOpenApi(headers, servletConfig, application, uriInfo, type);
        } catch (Exception e) {
            throw new OpenApiControllerException(e, Error.OPEN_API_ERROR);
        }
    }
}
