package com.arian.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class PropertyUtil {

   private static final String PROPERTIES_FILE = "application.properties";

   public String loadProperty(String key, String defaultValue) {
      final Properties properties = new Properties();
      try (InputStream input = PropertyUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
         if (input != null) {
            properties.load(input);
         }
      } catch (IOException e) {
         log.error(String.format("Error loading %s :: %s", PROPERTIES_FILE, e.getMessage()));
      }
      return properties.getProperty(key, defaultValue);
   }

}
