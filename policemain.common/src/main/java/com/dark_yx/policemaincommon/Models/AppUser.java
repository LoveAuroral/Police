package com.dark_yx.policemaincommon.Models;

/**
 * Created by Ligh on 2016/9/18 11:27
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */
public class AppUser {
    private String userName;
    private String password;
    private String cmsIP;

    @Override
    public String toString() {
        return "AppInfo{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", cmsIP='" + cmsIP + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCmsIP() {
        return cmsIP;
    }

    public void setCmsIP(String cmsIP) {
        this.cmsIP = cmsIP;
    }
}
