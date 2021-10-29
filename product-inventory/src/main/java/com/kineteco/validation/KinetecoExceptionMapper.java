package com.kineteco.validation;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class KinetecoExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
   @Override
   public Response toResponse(ConstraintViolationException e) {
      return Response.status(Response.Status.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(errorMessage(e)).build();
   }

   private JsonObject errorMessage(ConstraintViolationException e) {
      JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

      for (ConstraintViolation v : e.getConstraintViolations()) {
         objectBuilder.add(v.getPropertyPath().toString(), v.getMessage());
      }
      return objectBuilder.build();
   }
}
