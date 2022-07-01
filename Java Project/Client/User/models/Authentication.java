package com.dor.cmovies.models;

public class Authentication {

    private String userName;
    private String userPassword;
    private String userEmail;


    public Authentication() {}

    public Authentication(String userName, String Password)	{
        this.userName = userName;
        this.userPassword = Password;
    }

    public Authentication(String userName, String Password, String userEmail)	{
        this.userName = userName;
        this.userPassword = Password;
        this.userEmail = userEmail;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
