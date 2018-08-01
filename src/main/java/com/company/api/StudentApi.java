package com.company.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/student")
public class StudentApi {

    @GET
    public String getStudents() {
        return "{\"students\" : []}";
    }

}
