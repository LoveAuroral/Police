package com.dark_yx.policemain.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 视频用户
 */
@Table(name = "GroupCall")
public class User implements Serializable {
    @Column(name = "IMEI", isId = true)
    private String iMEI;

    @Column(name = "UserName")
    private String UserName;

    @Column(name = "RealName")
    private String RealName;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User() {
    }

    public User(String iMEI, String UserName, String RealName) {
        this.iMEI = iMEI;
        this.UserName = UserName;
        this.RealName = RealName;
    }

    public User(String userName, String realName) {
        UserName = userName;
        RealName = realName;
    }

    public String getiMEI() {
        return iMEI;
    }


    public void setiMEI(String iMEI) {
        this.iMEI = iMEI;
    }


    public String getUserName() {
        return UserName;
    }


    public void setUserName(String userName) {
        UserName = userName;
    }


    public String getRealName() {
        return RealName;
    }


    public void setRealName(String realName) {
        RealName = realName;
    }

    @Override
    public String toString() {
        return "User [iMEI=" + iMEI + ",id=" + id + ", UserName=" + UserName + ", RealName="
                + RealName + "]";
    }


}
