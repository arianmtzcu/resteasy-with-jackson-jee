package com.arian.example.client;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.internal.ClientConfiguration;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.arian.example.model.TrackData;
import com.arian.example.util.PropertyUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ViewScoped
@ManagedBean(name = "restfulClient")
public class ExternalApiRestClient implements Serializable {

   private static final long serialVersionUID = 1L;

   @Getter
   @Setter
   private String title;

   @Getter
   @Setter
   private String message;

   @PostConstruct
   public void init() {
      title = PropertyUtil.loadProperty("app.title", "Default Application Title");
   }

   public void executeOperation() {
      message = "";
      try {
         final Client client = createClientWithTimeouts();
         final WebTarget target = createTarget(client);

         final TrackData trackData = buildData();
         final Response response = sendRequest(target, trackData);
         handleResponse(response);

      } catch (Exception e) {
         message = String.format("An error occurred while calling the external REST service. %s", e.getMessage());

      } finally {
         log.info(message);
      }
   }

   private Client createClientWithTimeouts() {
      final ResteasyProviderFactory instance = ResteasyProviderFactory.getInstance();
      final ClientConfiguration clientConfig = new ClientConfiguration(instance);

      final int connectionTimeout = Integer.parseInt(PropertyUtil.loadProperty("api.connection-timeout", "4000"));
      final int readTimeout = Integer.parseInt(PropertyUtil.loadProperty("api.read-timeout", "8000"));

      clientConfig.property("resteasy.connection.timeout", connectionTimeout);
      clientConfig.property("resteasy.socket.timeout", readTimeout);

      return ClientBuilder.newBuilder().register(new ObjectMapperContextResolver()).withConfig((Configuration) clientConfig).build();
   }

   private WebTarget createTarget(final Client client) {
      final String host = PropertyUtil.loadProperty("api.host", "http://localhost:8080/api/v1");
      final String path = PropertyUtil.loadProperty("api.path", "/default_path");
      return client.target(host).path(path);
   }

   private TrackData buildData() {
      final Random random = new Random();
      return TrackData.builder().uuid(String.valueOf(UUID.randomUUID())).trackingCode(random.nextLong()).applicationCode(random.nextInt()).build();
   }

   private Response sendRequest(final WebTarget target, final TrackData trackData) {
      return target.request(APPLICATION_JSON).post(Entity.entity(trackData, APPLICATION_JSON));
   }

   private void handleResponse(final Response response) throws JsonProcessingException {
      if (response.getStatus() == 200) {
         message = response.readEntity(String.class);
         log.info(message);

         final ObjectMapper objectMapper = new ObjectMapper();
         final JsonNode rootNode = objectMapper.readTree(message);
         final String bodyContent = rootNode.path("body").asText();

         final TrackData trackData = objectMapper.readValue(bodyContent, TrackData.class);
         message = String.format("HTTP Response %s :: uuid=%s, tracking_code=%s, application_code=%s", response.getStatus(), trackData.getUuid(),
               trackData.getTrackingCode(), trackData.getApplicationCode());

      } else {
         message = String.format("Error in request >>> status=%s", response.getStatus());
      }

      response.close();
   }
}
