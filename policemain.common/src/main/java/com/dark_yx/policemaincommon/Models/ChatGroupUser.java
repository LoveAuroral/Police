package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by lmp on 2016/5/30.
 */
@Table(name = "ChatGroupUser")
public class ChatGroupUser {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "jid")
    private String jid;
    @Column(name = "groupName")
    private String groupName;

    public int getId() {
        return id;
    }

    public ChatGroupUser(String jid, String groupName) {
        this.jid = jid;
        this.groupName = groupName;
    }

    public ChatGroupUser() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    @Override
    public String toString() {
        return "ChatGroupUser{" +
                "id=" + id +
                ", jid='" + jid + '\'' +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
