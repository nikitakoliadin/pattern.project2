package com.qthegamep.pattern.project2.controller;

import com.qthegamep.pattern.project2.util.Paths;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Singleton
@Path(Paths.TEST_PATH)
public class HealthCheckerControllerImpl implements HealthCheckerController {

    @Override
    @GET
    public Response test() {
        return Response.status(Response.Status.OK)
                .build();
    }
}
