package com.kineteco;

import com.kineteco.api.Product;
import com.kineteco.api.ProductInventoryService;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

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
    @Fallback(fallbackMethod = "fallbackAvailable")
    public Boolean available(@PathParam("sku") String sku, @QueryParam("units") Integer units) {
       return productInventoryService.getStock(sku) >= units;
    }

    public Boolean fallbackAvailable(String sku, Integer units) {
        return units <= 5;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @CircuitBreaker(
          requestVolumeThreshold=3,
          failureRatio = 0.66,
          delay = 1,
          delayUnit = ChronoUnit.SECONDS
    )
    @Fallback(value = SalesServiceFallbackHandler.class)
    @Bulkhead(value=1)
    public Response createDeluxeCommand(CustomerCommand command) {
        Product product = productInventoryService.inventory(command.getSku());
        if ("DELUXE".equals(product.getProductLine())) {
            UUID uuid = UUID.randomUUID();
            LOGGER.infof("Deluxe product %s with %d units for customer %s created.", command.getSku(), command.getUnits(), command.getCustomerId());
            return Response.status(Response.Status.CREATED).entity(uuid).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
