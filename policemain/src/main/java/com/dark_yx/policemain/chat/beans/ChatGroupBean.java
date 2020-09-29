package com.dark_yx.policemain.chat.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by dark_yx-i on 2018/2/7.
 */

@Table(name = "chatGroup")
public class ChatGroupBean {
    @Column(name = "ids", isId = true)
    private int ids;

    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    public ChatGroupBean(String name) {
        this.name = name;
    }


    public long getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    @Column(name = "creatorUserId")
    private long creatorUserId;

    public ChatGroupBean() {
    }

    /**
     * chatGroupUsers : [{"chatGroupId":1,"userId":2,"user":{"name":"admin","userName":"admin","policeNo":null,"position":null,"phoneNumber":null,"id":2},"id":1},{"chatGroupId":1,"userId":4,"user":{"name":"李官辉","userName":"liguanhui","policeNo":"p002","position":"李官辉","phoneNumber":"17685302679","id":4},"id":3}]
     * id : 1
     */

    private List<ChatGroupUserBean> chatGroupUsers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ChatGroupUserBean> getChatGroupUsers() {
        return chatGroupUsers;
    }

    public void setChatGroupUsers(List<ChatGroupUserBean> chatGroupUsers) {
        this.chatGroupUsers = chatGroupUsers;
    }

    @Override
    public String toString() {
        return "ChatGroupBean{" +
                "ids=" + ids +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", chatGroupUsers=" + chatGroupUsers +
                '}';
    }
}
