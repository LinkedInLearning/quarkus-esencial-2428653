package com.kineteco.api;

import java.util.Objects;

public class Product {
   private String sku;
   private String productLine;

   public String getSku() {
      return sku;
   }

   public String getProductLine() {
      return productLine;
   }

   public void setSku(String sku) {
      this.sku = sku;
   }

   public void setProductLine(String productLine) {
      this.productLine = productLine;
   }

   @Override
   public String toString() {
      return "Product{" + "sku='" + sku + '\'' + ", productLine='" + productLine + '\'' + '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      Product product = (Product) o;
      return Objects.equals(sku, product.sku) && Objects.equals(productLine, product.productLine);
   }

   @Override
   public int hashCode() {
      return Objects.hash(sku, productLine);
   }
}