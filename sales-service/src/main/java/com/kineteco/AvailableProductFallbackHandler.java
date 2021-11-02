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
      String exceptionName = getExceptionName(context);
      LOGGER.debugf("Handle exception %s", exceptionName);

      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
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
