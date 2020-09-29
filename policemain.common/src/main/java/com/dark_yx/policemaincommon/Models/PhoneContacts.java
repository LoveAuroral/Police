package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by VicknL2 on 2016/8/24.
 */
@Table(name = "PhoneContacts")
public class PhoneContacts {
    @Column(name = "UserName")
    private String UserName;
    @Column(name = "PhoneNumber", isId = true)
    private String PhoneNumber;
    @Column(name = "SimNum")
    private String SimNum;
    @Column(name = "Unit")
    private String Unit;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getSimNum() {
        return SimNum;
    }

    public void setSimNum(String simNum) {
        SimNum = simNum;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    @Override
    public String toString() {
        return "PhoneContacts{" +
                "UserName='" + UserName + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", SimNum='" + SimNum + '\'' +
                ", Unit='" + Unit + '\'' +
                '}';
    }
}
