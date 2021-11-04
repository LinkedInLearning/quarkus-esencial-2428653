package com.kineteco.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ProductOrderStats {
    public String sku;
    public int units;

    public ProductOrderStats() {
        this.units = 0;
    }

    public ProductOrderStats(String sku, int units) {
        this.sku = sku;
        this.units = units;
    }
}