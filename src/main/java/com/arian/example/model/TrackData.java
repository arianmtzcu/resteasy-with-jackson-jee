package com.arian.example.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "uuid", "tracking_code", "application_code" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackData implements Serializable {

   private static final long serialVersionUID = 1L;

   @JsonProperty("uuid")
   String uuid;

   @JsonProperty("tracking_code")
   Long trackingCode;

   @JsonProperty("application_code")
   Integer applicationCode;

}
