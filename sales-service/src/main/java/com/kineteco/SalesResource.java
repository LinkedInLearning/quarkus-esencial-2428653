package com.kineteco;

import com.kineteco.api.ProductInventoryService;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sales")
public class SalesResource {

    private static final Logger LOGGER = Logger.getLogger(SalesResource.class);

    @Inject
    @RestClient
    ProductInventoryService productInventoryService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String health() {
        return "Sales Service is up!!";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{sku}/availability")
    @Timeout(value = 100)
    @Retry(retryOn = TimeoutException.class, delay = 100, jitter = 25)
    public Response available(@PathParam("sku") String sku, @QueryParam("units") Integer units) {
        LOGGER.debugf("available %s %d", sku, units);
        if (units == null) {
            throw new BadRequestException("units query parameter is mandatory");
        }
        return Response.ok(productInventoryService.getStock(sku) >= units).build();
    }

}