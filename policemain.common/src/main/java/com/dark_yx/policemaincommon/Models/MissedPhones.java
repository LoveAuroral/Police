package com.dark_yx.policemaincommon.Models;

/**
 * Created by X on 2018/7/10.
 */

public class MissedPhones {
    String phone;
    String name;



    public MissedPhones(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }

    @Override
    public String toString() {
        return "MissedPhones{" +
                "phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public MissedPhones() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
