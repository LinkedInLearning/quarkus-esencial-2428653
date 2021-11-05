package com.kineteco.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ProductOrderStats {
    public String sku;
    public int orderCount;

    public ProductOrderStats() {
        this.orderCount = 0;
    }
}