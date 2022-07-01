package org.java.app.movies.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Authentication {
	
	private String userName;
	private String userPassword;
	private String userStatus;
	private String userEmail;
		
	public Authentication() {}
	
	public Authentication(Authentication authentication)	{
		
		this.userStatus = authentication.getUserStatus();
		this.userName = authentication.getUserName();
		this.userPassword = authentication.getUserPassword();
		this.userEmail = authentication.getUserEmail();
	}
	
	public Authentication(String userName, String Password)	{
		
		this.userStatus = "guest";
		this.userName = userName;
		this.userPassword = Password;
	}
	
	public Authentication(String userName, String Password, String status)	{
		
		this.userStatus = status;
		this.userName = userName;
		this.userPassword = Password;
	}
	
	public Authentication(String userName, String Password, String UserEmail, String status) {
		
		this.userStatus = status;
		this.userName = userName;
		this.userEmail = UserEmail;
		this.userPassword = Password;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	public String getUserStatus() {
		return this.userStatus;
	}
	
	public void setUserStatus(String validation) {
		this.userStatus = validation;
	}
	
	public String getUserEmail() {
		return this.userEmail;
	}
	
	public void setUserEmail(String UserEmail) {
		this.userEmail = UserEmail;
	}
	
	public String toString() {
		return "Username: " + userName + "\tPassword: " + userPassword + "\tStatus: " + userStatus;
	}
}
