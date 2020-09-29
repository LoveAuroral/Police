package com.dark_yx.policemain.entity;

import java.io.Serializable;

/**
 * Created by Ligh on 2017/2/14 16:37
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class MUCInfo implements Serializable {
    private String account;
    private String room;
    private String nickname;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        if (nickname.contains("@")) {
            this.nickname = nickname.substring(0, account.indexOf("@"));
            return;
        }
        this.nickname = nickname;
    }

    public MUCInfo(String room) {
        this.room = room;
    }

    public MUCInfo() {
    }

    @Override
    public String toString() {
        return "MUCInfo{" +
                "account='" + account + '\'' +
                ", room='" + room + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
