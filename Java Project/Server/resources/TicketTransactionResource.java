package org.java.app.movies.resources;

import java.util.List;

import org.java.app.movies.model.ErrorMessage;
import org.java.app.movies.model.TicketTransaction;
import org.java.app.movies.services.TicketTransactionService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


@Path("/tickets")
public class TicketTransactionResource {

	TicketTransactionService ticketTransactionService = new TicketTransactionService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<TicketTransaction> getTickets() {

		List<TicketTransaction> list = ticketTransactionService.getAllTickets();
		
		if( list == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null Object returned", "TicketTransaction Resources class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		return list;
	}
	
	@GET
	@Path("/{userEmail}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TicketTransaction> getAllUserTickets(@PathParam("userEmail") String userEmail){

		List<TicketTransaction> list = ticketTransactionService.getAllUserTickets(userEmail);

		if( list == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null Object returned", "TicketTransaction Resources class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		return list;
	}
	
	@GET
	@Path("/{userName}/{userEmail}/{showID}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TicketTransaction> getAllShowTickets(@PathParam("userEmail") String userEmail, @PathParam("showID") int showID){
		
		List<TicketTransaction> list = ticketTransactionService.getAllShowTickets(showID);
		
		if( list == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null Object returned", "TicketTransaction Resources class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		return list;
	}
	
	@GET
	@Path("{userEmail}/{ticketID}") // get all show tickets
	@Produces(MediaType.APPLICATION_JSON)
	public TicketTransaction getTicket(@PathParam("userEmail") String userEmail, @PathParam("ticketID") long ticketID)	{
		TicketTransaction ticket = ticketTransactionService.getTicket(ticketID);
		
		if( ticket == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null Object returned", "TicketTransaction Resources class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		
		return ticket;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TicketTransaction addTicket(TicketTransaction ticket) throws Exception{
		
		if( ticket == null) {
			ErrorMessage errorMessage = new ErrorMessage(404, "Null Object returned", "TicketTransaction Resources class");	
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		try {
			TicketTransaction addedTicket = ticketTransactionService.addTicket(ticket);
			//getTickets();
			return addedTicket;
		}
		catch( Exception e) {
			e.printStackTrace();
		}
		return ticket;
	}
	
	@DELETE
	@Path("/{ticketID}")
	public void deleteTicket(@PathParam("ticketID") int ticketID) throws Exception{
		
		try {
			ticketTransactionService.deleteTicket(ticketID);
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
}
