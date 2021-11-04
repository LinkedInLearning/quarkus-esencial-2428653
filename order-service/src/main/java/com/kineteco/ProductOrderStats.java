package com.kineteco;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ProductOrderStats {
    public String sku;
    public int units;

    public ProductOrderStats() {
        this.units = 0;
    }
}