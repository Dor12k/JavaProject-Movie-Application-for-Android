package org.java.app.movies.exception;

import org.java.app.movies.model.ErrorMessage;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DataExecptionMapper implements ExceptionMapper<DataExecptions>{

	@Override
	public Response toResponse(DataExecptions exception) {

		ErrorMessage errorMessage = new ErrorMessage(404, exception.getMessage(), "emovies");	
		return Response.status(Status.NOT_FOUND).entity(errorMessage).build();
	}
}
