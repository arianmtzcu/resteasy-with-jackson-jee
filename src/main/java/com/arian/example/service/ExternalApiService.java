package com.arian.example.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.arian.example.model.TrackData;

@Consumes(value = MediaType.APPLICATION_JSON)
@Produces(value = MediaType.APPLICATION_JSON)
public interface ExternalApiService {

   @POST
   @Path(value = "/send_data")
   @Consumes(value = MediaType.APPLICATION_JSON)
   @Produces(value = MediaType.APPLICATION_JSON)
   Response sendData(TrackData model);

}
