package com.kineteco;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CustomerCommand {
   @NotNull
   private String customerId;
   @NotNull
   private String sku;
   @Positive
   private Integer units;

   public String getCustomerId() {
      return customerId;
   }

   public void setCustomerId(String customerId) {
      this.customerId = customerId;
   }

   public String getSku() {
      return sku;
   }

   public void setSku(String sku) {
      this.sku = sku;
   }

   public Integer getUnits() {
      return units;
   }

   public void setUnits(Integer units) {
      this.units = units;
   }
}