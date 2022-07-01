package com.dor.cmovies.models;

public class User {

    private String userName;
    private String firstName;
    private String lastName;
    private String userEmail;
    private String userPassword;
    private boolean isAdmin;

    public User() {}

    public User(User user) {

        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userEmail = user.getUserEmail();
        this.userPassword = user.getUserPassword();
        this.isAdmin = user.getIsAdmin();
    }

    public User(String nickName, String first, String last, String email, String password, boolean isAdmin) {

        this.userName = nickName;
        this.firstName = first;
        this.lastName = last;
        this.userEmail = email;
        this.userPassword = password;
        this.isAdmin = isAdmin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}

