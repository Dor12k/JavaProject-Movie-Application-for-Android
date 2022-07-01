package org.java.app.movies.resources;

import java.util.List;

//import javax.servlet.http.HttpServletResponse;


import org.java.app.movies.model.User;
import org.java.app.movies.model.Authentication;
import org.java.app.movies.model.ErrorMessage;
import org.java.app.movies.services.AuthenticationService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.WebApplicationException;


@Path("/authentication")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {
	
	AuthenticationService authenticationService = new AuthenticationService();

	
	@GET
	public List<User> getAuthentication() {
		
		List<User> list = authenticationService.getAllAuthentication();		
		if( list == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null authentication", "Authentication service class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		return list;
	}
	
	@PUT
	public Authentication getAuthentication(Authentication authentication) {

		if( authentication.getUserName() == null || authentication.getUserPassword() == null) {
			
			ErrorMessage errorMessage = new ErrorMessage(404, "Null authentication", "Authentication service class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		Authentication userValidation = new Authentication(authenticationService.validation(authentication));
		return userValidation;
	}
	
	@POST
	public Authentication register(User user) throws Exception {
		
		if( user.getUserName() == null || user.getUserPassword() == null) {
			
			ErrorMessage errorMessage = new ErrorMessage(404, "Null authentication", "Authentication service class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		
		Authentication userValidation = new Authentication(authenticationService.register(user));
		return userValidation;
	}

}
