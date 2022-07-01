package org.java.app.movies.resources;

import java.util.List;

import org.java.app.movies.model.ErrorMessage;
import org.java.app.movies.model.User;
import org.java.app.movies.services.UserService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
	
	UserService userService = new UserService();
	
	@GET
	public List<User> getUsers() {
		
		List<User> list = userService.getAllUsers();
		if( list == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null Object", "User Resources class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		return list;
	}
	
	@GET
	@Path("/{userName}")
	public User getUser(@PathParam("userName") String userName) {

		if( userName.indexOf('@') == -1) {
			return userService.getUser(userName);
		}
		return userService.getUserByEmail(userName);

	}
		
	@POST
	public User addUser(User user) throws Exception {
		
		if( user == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null Object", "User Resources class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		return userService.addUser(user);
	}
	
	@PUT	
	public User updateUser(User user) throws Exception {
		
		if( user == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null Object", "User Resources class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		return userService.updateUser(user);
	}
	
	@DELETE
	@Path("/{userName}")
	public void deleteUser(User user) throws Exception {
		
		if( user == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null Object", "User Resources class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		userService.deleteUser(user);
	}
}
