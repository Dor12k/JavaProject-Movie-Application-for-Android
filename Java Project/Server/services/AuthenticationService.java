package org.java.app.movies.services;



import java.util.List;

import org.java.app.movies.database.DataBaseClass;
import org.java.app.movies.model.Authentication;
import org.java.app.movies.model.User;

public class AuthenticationService {
	
	DataBaseClass data = new DataBaseClass();
	
	public List<User> getAllAuthentication() {
		
		List<User> list = data.getUsers();
		return list;
	}
	
	public Authentication getAuthentication(Authentication authentication)	{
			
		return authentication;
	}
	
	public Authentication validation(Authentication authentication){

		DataBaseClass login = new DataBaseClass();
		Authentication userValidation = new Authentication(login.validate(authentication));
		
		return userValidation;		
	}
	
	public Authentication register(User user) throws Exception{

		DataBaseClass login = new DataBaseClass();
		Authentication userValidation = new Authentication(login.register(user));
		
		return userValidation;		
	}
}
