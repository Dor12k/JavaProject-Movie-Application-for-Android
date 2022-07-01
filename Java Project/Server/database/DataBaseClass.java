package org.java.app.movies.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

import org.java.app.movies.model.User;
import org.java.app.movies.model.Show;
import org.java.app.movies.model.Ticket;
import org.java.app.movies.model.Authentication;
import org.java.app.movies.model.TicketTransaction;


public class DataBaseClass {
	
	private String dbUrl = "jdbc:mysql://localhost/movies";
	private String dbDriver = "com.mysql.cj.jdbc.Driver";
	private String dbUname  = "root";
	private String dbPassword = "root";
	
	public void loadDriver(String dbDriver) {
		try {
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(dbUrl, dbUname, dbPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	
	
	
	public List<Show> getShows() 
	{
		Show show;
		List<Show> list = new ArrayList<>();
		
		loadDriver(dbDriver);
		Connection con = getConnection();
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("select * from Shows");
			ResultSet rs = ps.executeQuery();
			while( rs.next() )
			{
				long id = rs.getLong("showID");
				String showName = rs.getString("showName");
				String artistName = rs.getString("artistName");
				String showDescription = rs.getString("showDescription");
				String showVenue = rs.getString("showVenue");
				String showDate = rs.getString("showDate");
				String showTimeStart = rs.getString("showTimeStart");
				double ticketPrice = rs.getDouble("ticketPrice");
				int numberOfTickets = rs.getInt("numberOfTickets");
				int ticketsInvited = rs.getInt("ticketsInvited");
				int ticketsAvailable = rs.getInt("ticketsAvailable");
				String imageUrl = rs.getString("imageURL");
				show = new Show(id, showName, artistName, showDescription, showVenue, showDate, showTimeStart, ticketPrice, numberOfTickets, ticketsInvited, ticketsAvailable, imageUrl);
				list.add(show);
			}
			con.close();
			return list;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public Show getShow(long showID) {
		
		Show show;
		loadDriver(dbDriver);
		PreparedStatement ps;
		Connection con = getConnection();
		

		try {	
			ps = con.prepareStatement("select * from Shows where showID = ?");
			ps.setLong(1, showID);
			ResultSet rs = ps.executeQuery();
			
			if( rs.next() )
			{
				long id = rs.getLong("showID");
				String showName = rs.getString("showName");
				String artistName = rs.getString("artistName");
				String showDescription = rs.getString("showDescription");
				String showVenue = rs.getString("showVenue");
				String showDate = rs.getString("showDate");
				String showTimeStart = rs.getString("showTimeStart");
				double ticketPrice = rs.getDouble("ticketPrice");
				int numberOfTickets = rs.getInt("numberOfTickets");
				int ticketsInvited = rs.getInt("ticketsInvited");
				int ticketsAvailable = rs.getInt("ticketsAvailable");
				String imageUrl = rs.getString("imageURL");
				show = new Show(id, showName, artistName, showDescription, showVenue, showDate, showTimeStart, ticketPrice, numberOfTickets, ticketsInvited, ticketsAvailable, imageUrl);
				con.close();
				return show;
			}		
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		show = new Show("SQL Eror");
		return show;	
	}
	
	public Show getShow(String showName) {
		
		Show show;
		loadDriver(dbDriver);
		Connection con = getConnection();
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("select * from Shows where showName = ?");
			ps.setString(1, showName);
			ResultSet rs = ps.executeQuery();
			
			if( rs.next() )
			{
				long id = rs.getLong("showID");
				String showname = rs.getString("showName");
				String artistName = rs.getString("artistName");
				String showDescription = rs.getString("showDescription");
				String showVenue = rs.getString("showVenue");
				String showDate = rs.getString("showDate");
				String showTimeStart = rs.getString("showTimeStart");
				double ticketPrice = rs.getDouble("ticketPrice");
				int numberOfTickets = rs.getInt("numberOfTickets");
				int ticketsInvited = rs.getInt("ticketsInvited");
				int ticketsAvailable = rs.getInt("ticketsAvailable");
				String imageUrl = rs.getString("imageURL");
				show = new Show(id, showname, artistName, showDescription, showVenue, showDate, showTimeStart, ticketPrice, numberOfTickets, ticketsInvited, ticketsAvailable, imageUrl);
				con.close();
				return show;
			}	
		}
		catch(SQLException e) {
			e.printStackTrace();

		}
		show = new Show("SQL Eror");
		return show;	
	}

	public Show addShow(Show show) throws Exception{
		
		if( 20 < show.getShowName().length() ) {
			System.out.println("Show name too long");
			return show;
		}

		loadDriver(dbDriver);
		Connection con = getConnection();
		PreparedStatement ps;
		
		con.setAutoCommit(false);	

		try {
			ps = con.prepareStatement("INSERT INTO shows (ShowName, ArtistName, ShowDescription, ShowVenue, ShowDate, ShowTimeStart, "
					+ "TicketPrice, numberOfTickets, ticketsInvited, ticketsAvailable, imageURL) VALUES (?, ?, ?, ? ,? ,? ,? ,? ,?, ?, ?)",
					  Statement.RETURN_GENERATED_KEYS);
			//ps.setLong(1, generatedKey);
			ps.setString(1, show.getShowName());
			ps.setString(2, show.getArtistName());
			ps.setString(3, show.getShowDescription());
			ps.setString(4, show.getShowVenue());
			ps.setString(5, show.getShowDate());
			ps.setString(6, show.getShowTimeStart());
			ps.setDouble(7, show.getTicketPrice());
			ps.setInt(8,  show.getNumberOfTickets());
			ps.setInt(9, show.getTicketsInvited());
			ps.setInt(10, show.getTicketsAvailable());
			ps.setString(11, show.getImageURL());
			ps.executeUpdate();


			long generatedKey = -1;
			ResultSet generatedKeys = ps.getGeneratedKeys();

			if( generatedKeys.next() ) {

				generatedKey = generatedKeys.getLong(1);
				PreparedStatement stmt1 = con.prepareStatement("UPDATE concerts.shows set showID=? where showName=?");
				stmt1.setLong(1, generatedKey);
				stmt1.setString(2, show.getShowName());
				show.setShowID(generatedKey);
			}
			con.commit();
			Show show1 = getShow(generatedKey);
			return show1;
		}
		catch(Exception e) {
			e.printStackTrace();
			con.rollback();
		}
		con.close();
		return show;
	}
	
	public Show updateShow(Show show) throws Exception{
		
		loadDriver(dbDriver);
		PreparedStatement ps;
		Connection con = getConnection();

		con.setAutoCommit(false);	

		try {
			ps = con.prepareStatement("UPDATE shows "
					+ "SET showName = ?, ArtistName = ?, ShowDescription = ?, ShowVenue = ?, ShowDate = ?, ShowTimeStart = ?,"
					+ "TicketPrice = ?, numberOfTickets = ?, ticketsInvited = ?, ticketsAvailable = ? , imageURL = ?"
					+ "WHERE ShowID = ?" );
			
			ps.setString(1, show.getShowName());
			ps.setString(2, show.getArtistName());
			ps.setString(3, show.getShowDescription());
			ps.setString(4, show.getShowVenue());
			ps.setString(5, show.getShowDate());
			ps.setString(6, show.getShowTimeStart());
			ps.setDouble(7, show.getTicketPrice());
			ps.setInt(8,  show.getNumberOfTickets());
			ps.setInt(9,  show.getTicketsInvited());
			ps.setInt(10, show.getTicketsAvailable());
			ps.setString(11, show.getImageURL());
			ps.setLong(12, show.getShowID());

			ps.executeUpdate();
			con.commit();

		}
		catch( Exception e) {
			e.printStackTrace();
			con.rollback();
		}
		con.close();
		return getShow(show.getShowID());
	}

	public void deleteShow(long showID) throws Exception{
		
		 	loadDriver(dbDriver);
			PreparedStatement ps;
			Connection con = getConnection();
				
			con.setAutoCommit(false);	
			
			try {
			ps = con.prepareStatement("DELETE FROM shows WHERE showID = ? ");
			ps.setLong(1, showID);
			ps.executeUpdate();
			con.commit();
			}
			catch(Exception e) {
				e.printStackTrace();
				con.rollback();
			}
			con.close();
	}
	
	public Show updateTickets(Show show, int ticketsInvited) throws Exception {

		loadDriver(dbDriver);
		PreparedStatement ps;
		Connection con = getConnection();
		
		con.setAutoCommit(false);
		
		show = getShow(show.getShowID());

		int invitedTickets = show.getTicketsInvited() + ticketsInvited;
		int availableTickets = show.getTicketsAvailable() - ticketsInvited;
				
		try {
			ps = con.prepareStatement("UPDATE shows SET ticketsInvited = ?, ticketsAvailable = ? WHERE showID = ?");
			
			ps.setInt(1, invitedTickets);
			ps.setInt(2, availableTickets);
			ps.setLong(3, show.getShowID());
			ps.executeUpdate();
			con.commit();
			con.close();
			show = getShow(show.getShowID());
			return show;
		}
		catch( Exception e) {
			e.printStackTrace();
			con.rollback();
		}
		con.close();
		return show;
	}
	

	
	
	

	public List<User> getUsers() 
	{	
		User user;
		boolean isAdmin;
		List<User> list = new ArrayList<>();
		
		ResultSet rs;
		PreparedStatement ps;
		loadDriver(dbDriver);
		Connection con = getConnection();
		
		try {
			ps = con.prepareStatement("select * from users");
			rs = ps.executeQuery();
			
			while( rs.next() )
			{
				if( rs.getString("IsAdmin").equals("yes") )
					isAdmin = true;
				else
					isAdmin = false;

				user = new User(rs.getString("UserName"), rs.getString("FirstName"), rs.getString("LastName"), 
								rs.getString("UserEmail"), rs.getString("UserPassword"), isAdmin);
				list.add(user);
			}
			
			con.close();
			return list;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public User getUser(String userName) {
		
		boolean isAdmin;
		loadDriver(dbDriver);
		Connection con = getConnection();
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("select * from users where UserName = ?");
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();
			if( rs.next() ){		
				
				if( rs.getString("IsAdmin").equals("yes") ) 
					isAdmin = true;
				else 
					isAdmin = false;

				User user = new User(rs.getString("UserName"),  rs.getString("FirstName"),    rs.getString("LastName"), 
									 rs.getString("UserEmail"), rs.getString("UserPassword"), isAdmin);
				con.close();
				return user;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return new User();	
	}
	
	public User getUserByMail(String userEmail) {
		
		boolean isAdmin;
		loadDriver(dbDriver);
		PreparedStatement ps;
		Connection con = getConnection();
		
		try {
			ps = con.prepareStatement("select * from users where UserEmail = ?");
			ps.setString(1, userEmail);
			ResultSet rs = ps.executeQuery();
			
			if( rs.next() ){		
				
				if( rs.getString("IsAdmin").equals("yes") ) 
					isAdmin = true;
				else 
					isAdmin = false;

				User user = new User(rs.getString("UserName"),  rs.getString("FirstName"),    rs.getString("LastName"), 
									 rs.getString("UserEmail"), rs.getString("UserPassword"), isAdmin);
				con.close();
				return user;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return new User();	
	}

	public User addUser(User user) throws Exception{
				
		loadDriver(dbDriver);
		Connection con = getConnection();
		PreparedStatement ps;
		
		con.setAutoCommit(false);	
		try {
			ps = con.prepareStatement("INSERT INTO users (UserName, FirstName, LastName, UserEmail, UserPassword, IsAdmin) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getFirstName());
			ps.setString(3, user.getLastName());
			ps.setString(4, user.getUserEmail());
			ps.setString(5, user.getUserPassword());
			
			if( user.getIsAdmin() ) 
				ps.setString(6, "yes");
			else 
				ps.setString(6, "no");

			ps.executeUpdate();			
			con.commit();
		}
		catch(Exception e) {
			e.printStackTrace();
			con.rollback();
		}
		con.close();
		return user;
	}
	
	public User updateUser(User user) throws Exception{
		
		loadDriver(dbDriver);
		Connection con = getConnection();
		PreparedStatement ps;
		
		con.setAutoCommit(false);	

		try {
			ps = con.prepareStatement("UPDATE concerts.users SET "
					+ "UserName = ?, FirstName = ?, LastName = ?, UserEmail = ?, UserPassword = ?, IsAdmin = ?,"
					+ "WHERE userName = ?");
			
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getFirstName());
			ps.setString(3, user.getLastName());
			ps.setString(4, user.getUserEmail());
			ps.setString(5, user.getUserEmail());
			ps.setString(6, user.getUserPassword());
			
			if( user.getIsAdmin() ) 
				ps.setString(7, "yes");
			else 
				ps.setString(7, "no");
			
			ps.executeUpdate();
			con.commit();
		}
		catch( Exception e) {
			e.printStackTrace();
			con.rollback();
		}
		con.close();
		return user;
	}
	
	public void deleteUser(User user) throws Exception{
		
	 	loadDriver(dbDriver);
		PreparedStatement ps;
		Connection con = getConnection();
			
		con.setAutoCommit(false);	
		
		try {
		ps = con.prepareStatement("DELETE FROM users WHERE UserName = ? ");
		ps.setString(1, user.getUserName());
		ps.executeUpdate();
		con.commit();
		}
		catch(Exception e) {
			e.printStackTrace();
			con.rollback();
		}
		con.close();
	}

	public Authentication register(User user) throws Exception {
		
		loadDriver(dbDriver);
		PreparedStatement ps;
		Connection con = getConnection();
				

		String validation = "geust";
		
		String username = user.getUserName();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String userEmail = user.getUserEmail();
		String userpassword = user.getUserPassword();
		
		try {
			
		ps = con.prepareStatement("select * from users where UserName = ? OR UserEmail = ?");
		ps.setString(1, username);
		ps.setString(2, userEmail);
		
		ResultSet rs = ps.executeQuery();		
		
			if( rs.next() )		// user name or email exist
			{
				String name = rs.getString("UserName");
				String email = rs.getString("UserEmail");
				
				if( username.equals(name) ) {	// right password
					
					validation = "Username is already exist ";
				}
				else if( userEmail.equals(email) ) {
					
					validation = "Email is already exist ";
				}
				con.close();
			}
			else {
				
				validation = "isUser"; // new user				
				User newUser = new User(username, firstName, lastName, userEmail, userpassword, false);
				
				con.close();
				addUser(newUser);
				
				Authentication authentication = new Authentication(username, userpassword, userEmail, validation);

				return authentication;
			}
			
			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Authentication authentication = new Authentication(username, userpassword, userEmail, validation);
		return authentication;
	}
	
	// return Authentication with status: admin/user/guest/worng password/user dosent exist/ 
	public Authentication validate(Authentication authentication) 
	{
		loadDriver(dbDriver);
		PreparedStatement ps;
		Connection con = getConnection();
				

		String validation = "geust";
		
		String username = authentication.getUserName();
		String userpassword = authentication.getUserPassword();
		
		try {
		ps = con.prepareStatement("select * from users where UserName = ?");
		ps.setString(1, username);
		
		ResultSet rs = ps.executeQuery();		
		
			if( rs.next() )		// user name exist
			{
				String email = rs.getString("UserEmail");
				String pass = rs.getString("UserPassword");
				
				if( pass.equals(userpassword) ) {	// right password
					
					if( rs.getString("IsAdmin").equals("yes") ) 
						validation = "isAdmin";
					else
						validation = "isUser";
					
					con.close();
					Authentication user = new Authentication(username, userpassword, email, validation);
					return user;
				}
				else 
					validation = "Worng Password";
			}
			else
				
				validation = "Username Doesn't exist";
			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		authentication.setUserStatus(validation);
		return new Authentication(authentication);
	}

	
	
	
	

	public List<TicketTransaction> getAllTickets(){
			
		Show show;
		User user;
		Ticket ticket;
		TicketTransaction ticketTransaction;
		List<TicketTransaction> list = new ArrayList<>();
		
		loadDriver(dbDriver);
		PreparedStatement ps;
		Connection con = getConnection();
		
		try {
			ps = con.prepareStatement("select * from tickets");
			ResultSet rs = ps.executeQuery();
			
			while( rs.next() )
			{

				long   showID = rs.getLong("ShowID");
				long   ticketID = rs.getLong("TicketID");

				int    seat = rs.getInt("seat");
				int    rowSeat = rs.getInt("rowSeat");
				int    quantity = rs.getInt("Quantity");
				
				double totalPrice = rs.getDouble("totalPrice");
				
				String userEmail = rs.getString("UserEmail");
				
				show = new Show(getShow(showID));
				user = new User(getUserByMail(userEmail));
				ticket = new Ticket(seat, rowSeat, show, "1/1/2022");
				ticketTransaction = new TicketTransaction(ticketID, ticket, user, quantity, totalPrice);

				list.add(ticketTransaction);
			}
			con.close();
			return list;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return list;

	}
	
	public List<TicketTransaction> getAllShowTickets(int showID){
		
		Show show;
		User user;
		Ticket ticket;
		TicketTransaction ticketTransaction;
		List<TicketTransaction> list = new ArrayList<>();
		
		loadDriver(dbDriver);
		PreparedStatement ps;
		Connection con = getConnection();
		
		try {
			ps = con.prepareStatement("select * from tickets WHERE showID = ?");
			ps.setInt(1, showID);

			ResultSet rs = ps.executeQuery();
			
			while( rs.next() )
			{

				long   showId = rs.getLong("ShowID");
				long   ticketID = rs.getLong("TicketID");

				int    seat = rs.getInt("seat");
				int    rowSeat = rs.getInt("rowSeat");
				int    quantity = rs.getInt("Quantity");	
				
				double totalPrice = rs.getDouble("totalPrice");
				
				String userEmail = rs.getString("UserEmail");
				
				show = new Show(getShow(showId));
				user = new User(getUserByMail(userEmail));
				ticket = new Ticket(seat, rowSeat, show, "1/1/2022");
				ticketTransaction = new TicketTransaction(ticketID, ticket, user, quantity, totalPrice);

				list.add(ticketTransaction);
			}
			con.close();
			return list;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<TicketTransaction> getAllUserTickets(String email){
		
		Show show;
		User user;
		Ticket ticket;
		TicketTransaction ticketTransaction;
		List<TicketTransaction> list = new ArrayList<>();
		
		loadDriver(dbDriver);
		PreparedStatement ps;
		Connection con = getConnection();
		
		try {
			ps = con.prepareStatement("select * from tickets WHERE userEmail = ?");
			ps.setString(1, email);

			ResultSet rs = ps.executeQuery();
			
			while( rs.next() )
			{

				long   showID = rs.getLong("ShowID");
				long   ticketID = rs.getLong("TicketID");

				int    seat = rs.getInt("seat");
				int    rowSeat = rs.getInt("rowSeat");
				int    quantity = rs.getInt("Quantity");
				
				double totalPrice = rs.getDouble("totalPrice");
				
				String userEmail = rs.getString("UserEmail");
				
				show = new Show(getShow(showID));
				user = new User(getUserByMail(userEmail));
				ticket = new Ticket(seat, rowSeat, show, "1/1/2022");
				ticketTransaction = new TicketTransaction(ticketID, ticket, user, quantity, totalPrice);

				list.add(ticketTransaction);
			}
			con.close();
			return list;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return list;
	}

	public TicketTransaction getTicket(long ticketID) {

		TicketTransaction ticketTransaction;
		
		loadDriver(dbDriver);
		Connection con = getConnection();
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("select * from Tickets where ticketID = ?");
			ps.setLong(1, ticketID);
			ResultSet rs = ps.executeQuery();
			if( rs.next() )
			{
				int    seat = rs.getInt("seat");
				int    rowSeat = rs.getInt("rowSeat");
				int    quantity = rs.getInt("Quantity");
				
				double totalPrice = rs.getDouble("totalPrice");
				
				String userEmail = rs.getString("UserEmail");


				long   showID = rs.getLong("ShowID");
				
				Show show = new Show(getShow(showID));
				User user = new User(getUserByMail(userEmail));
				Ticket ticket = new Ticket(seat, rowSeat, show, "1/1/2022");

				ticketTransaction = new TicketTransaction(ticketID, ticket, user, quantity, totalPrice);
				return ticketTransaction;
			}
			con.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return new TicketTransaction();
	}
	
	public void deleteTicket(int ticketID) throws Exception{
		
		loadDriver(dbDriver);
		Connection con = getConnection();
		PreparedStatement ps;
		
		con.setAutoCommit(false);	
		
		ps = con.prepareStatement("SELECT showID, quantity From tickets WHERE TicketID = ?");
		ps.setInt(1, ticketID);
		ResultSet rs = ps.executeQuery();
		
		if( rs.next() )
		{
			long showID = rs.getLong("showID");
			int quantity = rs.getInt("quantity");

			Show show = getShow(showID);

			updateTickets(show, -quantity);
		}	
		
		try {
			ps = con.prepareStatement("DELETE FROM tickets WHERE TicketID = ?");
			ps.setInt(1, ticketID);
			ps.executeUpdate();
			con.commit();
		}
		catch(SQLException e){
			e.printStackTrace();
			con.rollback();

		}
		con.close();
	}
	
	public TicketTransaction getLastTransaction() throws Exception {
		
		JdbcRowSet jdbcRowSet = RowSetProvider.newFactory().createJdbcRowSet();
		
		jdbcRowSet.setUrl(dbUrl);
		jdbcRowSet.setUsername(dbUname);
		jdbcRowSet.setPassword(dbPassword);
		
		try {
		jdbcRowSet.setCommand("select * from tickets");
		jdbcRowSet.execute();			
		
		if( jdbcRowSet.last() ) {
			int transactionID = jdbcRowSet.getInt("TicketID");
			TicketTransaction lastTransaction = new TicketTransaction(getTicket(transactionID));
			jdbcRowSet.close();
			return lastTransaction;
		}
		}catch( Exception e) {
			e.printStackTrace();
		}
		return new TicketTransaction();
	}

	public TicketTransaction addTicket(TicketTransaction ticketTransaction) throws Exception{
		
		loadDriver(dbDriver);
		PreparedStatement ps;
		Connection con = getConnection();
		
		con.setAutoCommit(false);	
		
		
		Show show = updateTickets(ticketTransaction.getTicket().getShow(), ticketTransaction.getQuantity());
		ticketTransaction.getTicket().setShow(show);

		int seat = ticketTransaction.getTicket().getSeat();
		int rowSeat = ticketTransaction.getTicket().getRowSeat();
		long showId = ticketTransaction.getTicket().getShow().getShowID();
		String showName = ticketTransaction.getTicket().getShow().getShowName();
		String showVenue = ticketTransaction.getTicket().getShow().getShowVenue();
		String showDate = ticketTransaction.getTicket().getShow().getShowDate();
		String showTimeStart = ticketTransaction.getTicket().getShow().getShowTimeStart();
		
		String firstName = ticketTransaction.getUser().getFirstName();
		String lastName = ticketTransaction.getUser().getLastName();
		String userEmail = ticketTransaction.getUser().getUserEmail();

		double ticketPrice =  ticketTransaction.getTicket().getShow().getTicketPrice();
		double totalPrice  =  ticketTransaction.getTotalPrice();
		int quantity = ticketTransaction.getQuantity();
		
		/*
		System.out.println("showId: " + showId);
		System.out.println("showName: " + showName);
		System.out.println("showVenue: " + showVenue);
		System.out.println("showDate: " + showDate);
		System.out.println("showTimeStart: " + showTimeStart);
		System.out.println("firstName: " + firstName);
		System.out.println("lastName: " + lastName);
		System.out.println("userEmail: " + userEmail);
		System.out.println("seat: " + seat);
		System.out.println("rowSeat: " + rowSeat);
		System.out.println("ticketPrice: " + ticketPrice);
		System.out.println("quantity: " + quantity);
		System.out.println("totalPrice: " + totalPrice);
		*/
		
		try {
			
			ps = con.prepareStatement("INSERT INTO tickets (ShowID, ShowName, ShowVenue, ShowDate, ShowTimeStart, FirstName, LastName, UserEmail, Seat, RowSeat, TicketPrice, Quantity, TotalPrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setLong(1,   showId);
			ps.setString(2, showName);
			ps.setString(3, showVenue);
			ps.setString(4, showDate);
			ps.setString(5, showTimeStart);
			
			ps.setString(6, firstName);
			ps.setString(7, lastName);
			ps.setString(8, userEmail);
			
			ps.setInt(9, seat);
			ps.setInt(10,  rowSeat);

			ps.setDouble(11, ticketPrice);
			ps.setInt(12,   quantity);
			
			ps.setDouble(13, totalPrice);
			
			ps.executeUpdate();

			long generatedKey = -1;
			ResultSet generatedKeys = ps.getGeneratedKeys();

			if( generatedKeys.next() ) {

				generatedKey = generatedKeys.getLong(1);
				PreparedStatement stmt1 = con.prepareStatement("UPDATE tickets set TicketID=? where ShowID=?");
				stmt1.setLong(1, generatedKey);
				stmt1.setLong(2, showId);
				ticketTransaction.setTransactionID(generatedKey);
			}
			con.commit();
			con.close();
			

			return getLastTransaction();
		}
		catch(Exception e) {
			e.printStackTrace();
			con.rollback();
		}
		con.close();
		return new TicketTransaction();
	}
}

