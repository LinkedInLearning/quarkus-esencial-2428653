package com.kineteco;

import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.jboss.logging.Logger;

import javax.ws.rs.core.Response;

public class AvailableProductFallbackHandler implements FallbackHandler<Response> {
   private static final Logger LOGGER = Logger.getLogger(AvailableProductFallbackHandler.class);

   @Override
   public Response handle(ExecutionContext context) {
      String exceptionName = "";
      if (context.getFailure().getCause() == null) {                  // <3>
         exceptionName = context.getFailure() .getClass().getSimpleName();
      } else {
         exceptionName = context.getFailure().getCause().getClass().getSimpleName();
      }

      LOGGER.debugf("Handle exception %s", exceptionName);

      if (exceptionName.equals(TimeoutException.class.getSimpleName())) {
         int units = (int) context.getParameters()[1];
         if (units <= 2) {
            return Response.ok(true).build();
         } else {
            return Response.status(Response.Status.GATEWAY_TIMEOUT).build();
         }
      }

      if (exceptionName.equals("ResteasyWebApplicationException")) {
         return Response.status(Response.Status.BAD_GATEWAY).build();
      }

      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
   }
}
