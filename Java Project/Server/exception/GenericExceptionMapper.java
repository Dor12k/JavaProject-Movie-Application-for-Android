package org.java.app.movies.exception;

import org.java.app.movies.model.ErrorMessage;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;

public class GenericExceptionMapper implements ExceptionMapper<Throwable>{

	@Override
	public Response toResponse(Throwable exception) {

		ErrorMessage errorMessage = new ErrorMessage(500, exception.getMessage(), "com.dor.emovies");	
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
	}	
}
