package com.kineteco;

import com.kineteco.api.Product;
import com.kineteco.api.ProductInventoryService;
import com.kineteco.fallbacks.SalesServiceFallbackHandler;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
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
    @Timeout(value = 100)
    @Retry(retryOn = TimeoutException.class, delay = 100, jitter = 25)
    @Fallback(value = SalesServiceFallbackHandler.class)
    public Response available(@PathParam("sku") String sku, @QueryParam("units") Integer units) {
        LOGGER.debugf("available %s %d", sku, units);
        if (units == null) {
            throw new BadRequestException("units query parameter is mandatory");
        }
        return Response.ok(productInventoryService.getStock(sku) >= units).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(value = 100)
    @Fallback(value = SalesServiceFallbackHandler.class)
    public Response createDeluxeCommand(CustomerCommand command) {
        Product product = productInventoryService.inventory(command.getSku());

        if ("DELUXE".equals(product.getProductLine())) {
            // Simulación
            LOGGER.infof("Deluxe product %s with %d units for customer %s created.", command.getSku(), command.getUnits(), command.getCustomerId());
            UUID uuid = UUID.randomUUID();

            return Response.created(URI.create(uuid.toString())).entity(uuid).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}