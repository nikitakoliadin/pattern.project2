package com.qthegamep.pattern.project2.controller;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

@FunctionalInterface
public interface HealthCheckerController {

    @GET
    Response test();
}
