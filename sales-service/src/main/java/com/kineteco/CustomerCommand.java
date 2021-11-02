package com.kineteco;

public class CustomerCommand {
   private String customerId;
   private String sku;
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