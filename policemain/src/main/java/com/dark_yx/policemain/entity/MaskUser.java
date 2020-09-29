package com.dark_yx.policemain.entity;

/**
 * Created by Ligh on 2016/9/7 15:13
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */
public class MaskUser {
    private String userName;//用户名
    private String realName;//屏蔽人姓名
    private String imei;
    private String id;

    @Override
    public String toString() {
        return "MaskUser{" +
                "userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", imei='" + imei + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}

