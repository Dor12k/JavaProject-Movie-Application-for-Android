package org.java.app.movies.services;

import java.util.List;

import org.java.app.movies.database.DataBaseClass;
import org.java.app.movies.model.User;


public class UserService {

	DataBaseClass data = new DataBaseClass();
	
	public List<User> getAllUsers() {
		
		List<User> list = data.getUsers();	
		return list;
	}
	
	public User getUser(String userName) {
		
		User user =  data.getUser(userName);
		return user;
	}
	
	public User getUserByEmail(String email) {
		
		User user = data.getUserByMail(email);
		return user;
	}
		
	public User addUser(User user) throws Exception {
		
		User addedUser = data.addUser(user);
		return addedUser;
	}

	public User updateUser(User user) throws Exception {
		
		User updatedUser = data.updateUser(user);
		return updatedUser;
	}

	public void deleteUser(User user) throws Exception {
		
		data.updateUser(user);
	}
	
}