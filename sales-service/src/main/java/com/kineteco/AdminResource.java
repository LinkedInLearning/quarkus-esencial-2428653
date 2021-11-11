package com.kineteco;

import io.quarkus.security.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/admin")
public class AdminResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("stats")
    public String adminStats() {
        // Admin task

        return "Admin stats calculation";
    }
}