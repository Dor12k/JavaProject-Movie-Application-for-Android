package org.java.app.movies.services;

import java.util.List;

import org.java.app.movies.database.DataBaseClass;
import org.java.app.movies.model.Show;
import org.java.app.movies.model.Ticket;
import org.java.app.movies.model.TicketTransaction;
import org.java.app.movies.model.User;



public class ShowService {

	DataBaseClass data = new DataBaseClass();
	UserService userService = new UserService();
		
		public List<Show> getAllShows() {
			List<Show> list = data.getShows();
			return list;
		}
				
		public Show getShow(String showName) {
			//Show a1 = new Show(12, "Mortal Kombat", "Dani", "Movie", "Yes Planet", "12/12/21", "18:00", 12.50, 250, 0, 250 );
			Show show = data.getShow(showName);
			return show;
		}
		
		public Show getShow(long showID) {
			Show show = data.getShow(showID);
			return show;
		}
		
		public TicketTransaction getShowTickets(String userName, int showID, int quantity) throws Exception {
			
			Show show = new Show(getShow(showID));
			Ticket ticket = new Ticket(show, "1/1/20");
			User user = new User(userService.getUser(userName));
			TicketTransaction newTransaction = new TicketTransaction(0, ticket, user, quantity, -1);
			
			TicketTransaction ticketTransaction = new TicketTransaction(data.addTicket(newTransaction));

			return ticketTransaction;
		}
		
		public Show updateTickets(Show show, int ticketsInvited) throws Exception{
			
			Show updatedShow = data.updateTickets(show, ticketsInvited);
			return updatedShow;
		}
		
		public Show updateShow(Show show) throws Exception{
			
			Show updatedShow = data.updateShow(show);
			return updatedShow;
		}
		
		public void deleteShow(long id) throws Exception{
					
			try{
				data.deleteShow(id);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	
		public Show addShow(Show show) throws Exception{
			
			try{
				Show addedShow = data.addShow(show);
				return addedShow;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return show;
		}
}
