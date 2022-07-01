package org.java.app.movies.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TicketTransaction {

	private int quantity = 0;
	private long transactionID = 0;
	private double totalPrice = 0;

	private Ticket ticket = new Ticket();
	private User user = new User();
	
	public TicketTransaction() {}
	
	public TicketTransaction(TicketTransaction ticketTransaction) {
		
		this.transactionID = ticketTransaction.getTransactionID();
		this.totalPrice = ticketTransaction.getTotalPrice();
		this.quantity = ticketTransaction.getQuantity();
		this.ticket = ticketTransaction.getTicket();
		this.user = ticketTransaction.getUser();
	}
	
	public TicketTransaction(Ticket ticket, User user, int quantity, double totalPrice) {

		this.user= user;
		this.ticket = ticket;
		this.quantity = quantity;		
		this.totalPrice = totalPrice;
	}
	
	public TicketTransaction(long transactionID, Ticket ticket, User user, int quantity, double totalPrice) {
		
		this.transactionID = transactionID;
		this.totalPrice = totalPrice;
		this.quantity = quantity;
		this.ticket = ticket;
		this.user= user;
	}
	
	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(long transactionID) {
		this.transactionID = transactionID;
	}

	public double getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}	
}
