package com.qthegamep.pattern.project2.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/test")
public class HealthCheckerControllerImpl implements HealthCheckerController {

    @Override
    @GET
    public Response test() {
        return Response.status(Response.Status.OK)
                .build();
    }
}
