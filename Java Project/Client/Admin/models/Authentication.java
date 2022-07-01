package com.dor.cmovies.models;

public class Authentication {

    private String userName;
    private String userPassword;

    public Authentication() {}

    public Authentication(String userName, String Password)	{
        this.userName = userName;
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
}
