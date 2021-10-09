package com.kineteco;

import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

import javax.ws.rs.core.Response;

public class SalesServiceFallbackHandler implements FallbackHandler<Response> {

   @Override
   public Response handle(ExecutionContext context) {
      if (context.getFailure() instanceof TimeoutException) {
         return Response.status(Response.Status.GATEWAY_TIMEOUT).build();
      }

      if (context.getFailure() instanceof CircuitBreakerOpenException) {
         return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
      }

      return Response.serverError().build();
   }
}
