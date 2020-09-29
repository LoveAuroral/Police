package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Ligh on 2018/3/9 13:57
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

@Table(name = "PrivatePhone")
public class PrivatePhoneBean {
    /**
     * name : 李官辉
     * phoneNumber : 17685302679
     * phoneWhiteType : 0
     */
    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "time")
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private int phoneWhiteType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPhoneWhiteType() {
        return phoneWhiteType;
    }

    public void setPhoneWhiteType(int phoneWhiteType) {
        this.phoneWhiteType = phoneWhiteType;
    }

    @Override
    public String toString() {
        return "PrivatePhoneBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneWhiteType=" + phoneWhiteType +
                '}';
    }
}
