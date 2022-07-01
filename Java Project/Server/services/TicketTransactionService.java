package org.java.app.movies.services;

import java.util.List;

import org.java.app.movies.database.DataBaseClass;
import org.java.app.movies.model.TicketTransaction;


public class TicketTransactionService {

	DataBaseClass data = new DataBaseClass();
		
		public List<TicketTransaction> getAllTickets() {

			List<TicketTransaction> list = data.getAllTickets();
			return list;
		}
		
		public List<TicketTransaction> getAllUserTickets(String userEmail){
			
			List<TicketTransaction> list = data.getAllUserTickets(userEmail);
			return list;
		}
		
		public List<TicketTransaction> getAllShowTickets(int showID){
			
			List<TicketTransaction> list = data.getAllShowTickets(showID);
			return list;
		}
		
 		public TicketTransaction getTicket(long ticketID) {
			TicketTransaction ticket = data.getTicket(ticketID);
			return ticket;
		}
				
		public void deleteTicket(int ticketID) throws Exception{
					
			try{
				data.deleteTicket(ticketID);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	
		public TicketTransaction addTicket(TicketTransaction ticket) throws Exception{
						
			try{
				TicketTransaction addedTicket = data.addTicket(ticket);
				return addedTicket;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return ticket;
		}
}
