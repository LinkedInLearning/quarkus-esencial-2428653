package com.kineteco;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class ManufactureOrderDeserializer extends ObjectMapperDeserializer<ManufactureOrder> {
    public ManufactureOrderDeserializer() {
        super(ManufactureOrder.class);
    }
}
