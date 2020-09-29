package com.dark_yx.policemain.chat.beans;

import com.dark_yx.policemaincommon.Models.UsersBean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by dark_yx-i on 2018/2/7.
 */

@Table(name = "chatGroupUser")
public class ChatGroupUserBean {

    /**
     * chatGroupId : 1
     * userId : 2
     * user : {"name":"admin","userName":"admin","policeNo":null,"position":null,"phoneNumber":null,"id":2}
     * id : 1
     */

    @Column(name = "chatGroupId")
    private int chatGroupId;

    @Column(name = "userId")
    private int userId;

    private UsersBean user;

    @Column(name = "id", isId = true, autoGen = false)
    private int id;

    public int getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(int chatGroupId) {
        this.chatGroupId = chatGroupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UsersBean getUser() {
        return user;
    }

    public void setUser(UsersBean user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ChatGroupUserBean{" +
                "chatGroupId=" + chatGroupId +
                ", userId=" + userId +
                ", user=" + user +
                ", id=" + id +
                '}';
    }
}
