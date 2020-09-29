package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by VicknL2 on 2016/8/24.
 */
@Table(name = "PhoneWhites")
public class PhoneWhites {
    @Column(name = "ID", isId = true, autoGen = false)
    private String ID;

    @Column(name = "UserName")
    private String UserName;

    @Column(name = "PhoneNumber")
    private String PhoneNumber;

    @Column(name = "Type")
    private String Type;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public PhoneWhites() {
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "PhoneWhites{" +
                "ID=" + ID +
                ", UserName='" + UserName + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }
}
