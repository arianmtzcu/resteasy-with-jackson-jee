package com.arian.example.client;

import javax.ws.rs.ext.ContextResolver;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

   private final ObjectMapper mapper;

   public ObjectMapperContextResolver() {
      this.mapper = createObjectMapper();
   }

   private ObjectMapper createObjectMapper() {
      final ObjectMapper mapper = new ObjectMapper();
      mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
      mapper.configure(MapperFeature.USE_ANNOTATIONS, true);
      mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).addHandler(new DeserializationProblemHandler() {

         @Override
         public boolean handleUnknownProperty(final DeserializationContext ctxt, final JsonParser jp, final JsonDeserializer<?> deserializer,
               final Object beanOrClass, String propertyName) {
            log.warn("Unknown property '{}' mapping to {}", propertyName, beanOrClass.getClass().getName());
            // Indicates that the unknown property was handled, so an exception will not be thrown
            return true;
         }
      });

      mapper.findAndRegisterModules();
      return mapper;
   }

   @Override
   public ObjectMapper getContext(Class<?> type) {
      return mapper;
   }

}
