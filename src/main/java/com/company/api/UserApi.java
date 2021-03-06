package com.company.api;

import com.company.dao.UserDao;
import com.company.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Path("/user")
public class UserApi {

    private Gson gson = new GsonBuilder().create();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addNewUser(@FormParam("firstName") String firstName,
                           @FormParam("secondName") String secondName,
                           @FormParam("age") int age) {
        User user = new User(firstName,secondName, age);

        String path = System.getProperty("java.io.tmpdir");
        System.out.println("UserApiTag path: " + path);

        File file = new File(path, "data.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(gson.toJson(user));
            System.out.println("UserApiTag file location: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("UserApiTag error: " + String.valueOf(e));
        }

        UserDao.getInstance().addUser(user);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers() {
        return gson.toJson(UserDao.getInstance().getUsers());
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(String jsonUser) {
        try {
            User user = gson.fromJson(jsonUser, User.class);
            if (UserDao.getInstance().update(user)) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{secondName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUser(@PathParam("secondName") String secondName) {
        if (UserDao.getInstance().remove(secondName)) {
            String json = "{\"result\" : \"Removed user with secondName: " + secondName + "\"}";
            return Response.status(Response.Status.OK).entity(json).build();
        } else {
            String json = "{\"result\" : \"User not found: " + secondName + "\"}";
            return Response.status(Response.Status.NOT_FOUND).entity(json).build();
        }
    }

}
