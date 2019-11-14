package com.ngxtech.homeautomation.bean;

public class UserData {

    private String user_id;

    private String user;
    private String password;
    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public UserData(String user_id, String user, String password) {
        this.user_id = user_id;
        this.user = user;
        this.password = password;
    }

    public UserData() {
    }
}
