package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by dark_yx-i on 2018/2/6.
 */

@Table(name = "user")
public class UsersBean {

    /**
     * name : 李官辉
     * userName : liguanhui
     * policeNo : p002
     * position : 李官辉
     * phoneNumber : 17685302679
     * id : 4
     */

    @Column(name = "name")
    private String name;

    @Column(name = "userName")
    private String userName;

    @Column(name = "policeNo")
    private String policeNo;

    @Column(name = "position")
    private String position;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "id")
    private long id;

    @Column(name = "ids", isId = true)
    private int ids;

    @Column(name = "landline")//座机
    private String landline;

    public UsersBean() {
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public UsersBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPoliceNo() {
        return policeNo;
    }

    public void setPoliceNo(String policeNo) {
        this.policeNo = policeNo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UsersBean{" +
                "name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", policeNo='" + policeNo + '\'' +
                ", position='" + position + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id=" + id +
                ", ids=" + ids +
                ", landline='" + landline + '\'' +
                '}';
    }
}
