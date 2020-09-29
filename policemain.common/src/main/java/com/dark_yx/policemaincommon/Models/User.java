package com.dark_yx.policemaincommon.Models;

/**
 * Created by Lmp on 2016/6/10.
 */
public class User {
    private String userName;

    private String userPwd;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User(String userName, String userPwd, String token) {
        this.userName = userName;
        this.userPwd = userPwd;
        this.token = token;
    }

    public User(String userName, String userPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }


    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
