package org.java.app.movies.resources;

import java.util.List;

import org.java.app.movies.model.ErrorMessage;
import org.java.app.movies.model.Show;
import org.java.app.movies.model.TicketTransaction;
import org.java.app.movies.services.ShowService;
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



@Path("/shows")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShowResource {

	ShowService showService = new ShowService();
	UserService userService = new UserService();
	
	
	@GET
	public List<Show> getShows() {
		
		List<Show> list = showService.getAllShows();
		
		if( list == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null Object", "Show Resources class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		
		return list;
	}
	
	@GET
	@Path("/{showID}")
	public Show getShow(@PathParam("showID") long showID)	{
		
		Show show = showService.getShow(showID);
		return show;
	}
	
	@DELETE
	@Path("/{showID}")
	public String deleteShow(@PathParam("showID") long showID) throws Exception{

		try {
			showService.deleteShow(showID);
		}
		catch( Exception e)	{
			e.printStackTrace();
		}
		return "Deleted";
	}
	
	@POST
	public Show addShow(Show show) throws Exception{
		
		if( show == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null Object given", "Show Resources class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		
		try {
			Show addedShow = showService.addShow(show);
			getShows();
			if( addedShow == null) {
				ErrorMessage errorMessage = new ErrorMessage(404, "Null Object returned", "Show Resources class");	
				Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
				throw new WebApplicationException(response);
			}
			return addedShow;
		}
		catch( Exception e) {
			e.printStackTrace();
		}
		return show;
	}
	
	@PUT
	public Show updateShow(Show show) throws Exception{
		
		if( show == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null Object given", "Show Resources class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		
		try {
			Show updatedShow = showService.updateShow(show);
			if( updatedShow == null) {
				ErrorMessage errorMessage = new ErrorMessage(404, "Null Object returned", "Show Resources class");	
				Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
				throw new WebApplicationException(response);
			}
			return updatedShow;
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
		return show;
	}

	@PUT
	@Path("/{showID}/{quantity}")
	public TicketTransaction getShowTickets(@PathParam("username") String userName, @PathParam("password") String password, @PathParam("showID") int showID, @PathParam("quantity") int quantity)
	throws Exception {
		try {
			TicketTransaction ticketTransaction = showService.getShowTickets(userName, showID, quantity);
			if( ticketTransaction == null) {
				ErrorMessage errorMessage = new ErrorMessage(404, "Null Object", "Show Resources class");	
				Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
				throw new WebApplicationException(response);
			}
			else
				return ticketTransaction;
		}catch( Exception e) {
			e.printStackTrace();
		}
		return new TicketTransaction();
	}
	
}
