package com.kineteco.fallbacks;

import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;
import org.jboss.logging.Logger;

import javax.ws.rs.core.Response;

public class SalesServiceFallbackHandler implements FallbackHandler<Response> {

   private static final Logger LOGGER = Logger.getLogger(SalesServiceFallbackHandler.class);

   @Override
   public Response handle(ExecutionContext context) {
      String exceptionName = getExceptionName(context);
      LOGGER.debugf("Handle exception %s", exceptionName);
      Response response;

      switch (exceptionName) {
         case "TimeoutException" :
            response = timeout(context);
            break;
         case "CircuitBreakerOpenException":
            response = Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
            break;
         case "BulkheadException":
            response = Response.status(Response.Status.TOO_MANY_REQUESTS).build();
            break;
         case "ResteasyWebApplicationException":
            response =  Response.status(Response.Status.BAD_GATEWAY).build();
            break;
         default:
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
      return response;
   }

   private Response timeout(ExecutionContext context) {
      Response response = Response.status(Response.Status.GATEWAY_TIMEOUT).build();
      if ("available".equals(context.getMethod().getName())) {
         int units = (int) context.getParameters()[1];
         if (units <= 2) {
            response = Response.ok(true).build();
         }
      }
      return response;
   }

   private String getExceptionName(ExecutionContext context) {
      String exceptionName = "";
      if (context.getFailure().getCause() == null) {
         exceptionName = context.getFailure() .getClass().getSimpleName();
      } else {
         exceptionName = context.getFailure().getCause().getClass().getSimpleName();
      }
      return exceptionName;
   }
}
