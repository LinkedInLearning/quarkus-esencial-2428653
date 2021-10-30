package com.kineteco.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UnitsAvailable {
   public final int unitsAvailable;

   public UnitsAvailable(int unitsAvailable) {
      this.unitsAvailable = unitsAvailable;
   }
}
