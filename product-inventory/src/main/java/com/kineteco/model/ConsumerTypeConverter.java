package com.kineteco.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

@Converter
public class ConsumerTypeConverter implements AttributeConverter<List<ConsumerType>, String> {

   @Override
   public String convertToDatabaseColumn(List<ConsumerType> consumerTypes) {
      if (consumerTypes == null || consumerTypes.isEmpty())
         return null;

      String result = "";
      for (ConsumerType c : consumerTypes) {
         result = result + "|" + c.name();
      }
      return result;
   }

   @Override
   public List<ConsumerType> convertToEntityAttribute(String dbData) {
      List targetConsumers = new ArrayList<>();
      if (dbData == null)
         return targetConsumers;

      for(ConsumerType consumerType: ConsumerType.values()) {
         parseConsumerTypes(dbData, consumerType, targetConsumers);
      }
      return targetConsumers;
   }

   private void parseConsumerTypes(String values, ConsumerType consumerType, List<ConsumerType> targetConsumers) {
      if (values.contains(consumerType.name())) {
         targetConsumers.add(consumerType);
      }
   }
}