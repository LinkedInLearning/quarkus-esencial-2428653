package com.kineteco.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

@RegisterForReflection
public class ProductInventory {
   private String sku;
   private String category;
   private String name;
   private int quantity;
   private String powerWatts;
   private String footprint;
   private BigDecimal manufacturingCost;
   private BigDecimal price;
   private ProductLine productLine;
   private ConsumerType[] targetConsumer;
   private ProductAvailability productAvailability;
   private int unitsAvailable;

   public ProductInventory() {

   }

   public ProductInventory(String sku) {
      this.sku = sku;
   }

   public ProductInventory(String sku, String category, String name, int quantity, String powerWatts, String footprint,
                           BigDecimal manufacturingCost, BigDecimal price, ProductLine productLine,
                           ConsumerType[] targetConsumer, ProductAvailability productAvailability, int unitsAvailable) {
      this.sku = sku;
      this.category = category;
      this.name = name;
      this.quantity = quantity;
      this.powerWatts = powerWatts;
      this.footprint = footprint;
      this.manufacturingCost = manufacturingCost;
      this.price = price;
      this.productLine = productLine;
      this.targetConsumer = targetConsumer;
      this.productAvailability = productAvailability;
      this.unitsAvailable = unitsAvailable;
   }

   @Override
   public String toString() {
      return "ProductInventory{" + "sku='" + sku + '\'' + ", category='" + category + '\'' + ", name='" + name + '\''
            + ", quantity=" + quantity + ", powerWatts=" + powerWatts + ", footprint=" + footprint
            + ", manufacturingCost=" + manufacturingCost + ", price=" + price + ", productLine=" + productLine
            + ", targetConsumer=" + Arrays.toString(targetConsumer) + ", productAvailability=" + productAvailability
            + ", unitsAvailable=" + unitsAvailable + '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      ProductInventory that = (ProductInventory) o;
      return quantity == that.quantity &&  Objects.equals(powerWatts,that.powerWatts) && Objects.equals(footprint, that.footprint)
            && unitsAvailable == that.unitsAvailable && Objects.equals(sku, that.sku) && Objects
            .equals(category, that.category) && Objects.equals(name, that.name) && Objects
            .equals(manufacturingCost, that.manufacturingCost) && Objects.equals(price, that.price)
            && productLine == that.productLine && Arrays.equals(targetConsumer, that.targetConsumer)
            && productAvailability == that.productAvailability;
   }

   @Override
   public int hashCode() {
      int result = Objects
            .hash(sku, category, name, quantity, powerWatts, footprint, manufacturingCost, price, productLine,
                  productAvailability, unitsAvailable);
      result = 31 * result + Arrays.hashCode(targetConsumer);
      return result;
   }

   public String getSku() {
      return sku;
   }

   public void setSku(String sku) {
      this.sku = sku;
   }

   public String getCategory() {
      return category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getQuantity() {
      return quantity;
   }

   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }

   public String getPowerWatts() {
      return powerWatts;
   }

   public void setPowerWatts(String powerWatts) {
      this.powerWatts = powerWatts;
   }

   public String getFootprint() {
      return footprint;
   }

   public void setFootprint(String footprint) {
      this.footprint = footprint;
   }

   public BigDecimal getManufacturingCost() {
      return manufacturingCost;
   }

   public void setManufacturingCost(BigDecimal manufacturingCost) {
      this.manufacturingCost = manufacturingCost;
   }

   public BigDecimal getPrice() {
      return price;
   }

   public void setPrice(BigDecimal price) {
      this.price = price;
   }

   public ProductLine getProductLine() {
      return productLine;
   }

   public void setProductLine(ProductLine productLine) {
      this.productLine = productLine;
   }

   public ConsumerType[] getTargetConsumer() {
      return targetConsumer;
   }

   public void setTargetConsumer(ConsumerType[] targetConsumer) {
      this.targetConsumer = targetConsumer;
   }

   public ProductAvailability getProductAvailability() {
      return productAvailability;
   }

   public void setProductAvailability(ProductAvailability productAvailability) {
      this.productAvailability = productAvailability;
   }

   public int getUnitsAvailable() {
      return unitsAvailable;
   }

   public void setUnitsAvailable(int unitsAvailable) {
      this.unitsAvailable = unitsAvailable;
   }
}
