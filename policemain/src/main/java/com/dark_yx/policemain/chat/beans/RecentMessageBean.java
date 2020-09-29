package com.dark_yx.policemain.chat.beans;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by dark_yx-i on 2018/2/7.
 */

@Table(name = "recentMessage")
public class RecentMessageBean {
    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "sendObjectId")
    private long sendObjectId;

    @Column(name = "type")
    private int type;

    @Column(name = "message")
    private String message;

    @Column(name = "chatSendType")
    private int chatSendType;

    @Column(name = "chatMessageType")
    private int chatMessageType;

    @Column(name = "isRead")
    private boolean isRead;

    @Column(name = "creationTime")
    private String creationTime;

    @Column(name = "tickets")
    private long tickets;

    @Column(name = "sendName")
    private String sendName;

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public long getSendObjectId() {
        return sendObjectId;
    }

    public void setSendObjectId(long sendObjectId) {
        this.sendObjectId = sendObjectId;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getChatSendType() {
        return chatSendType;
    }

    public void setChatSendType(int chatSendType) {
        this.chatSendType = chatSendType;
    }

    public int getChatMessageType() {
        return chatMessageType;
    }

    public void setChatMessageType(int chatMessageType) {
        this.chatMessageType = chatMessageType;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getTickets() {
        return tickets;
    }

    public void setTickets(long tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "RecentMessageBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sendObjectId=" + sendObjectId +
                ", type=" + type +
                ", message='" + message + '\'' +
                ", chatSendType=" + chatSendType +
                ", chatMessageType=" + chatMessageType +
                ", isRead=" + isRead +
                ", creationTime='" + creationTime + '\'' +
                ", tickets=" + tickets +
                ", sendName='" + sendName + '\'' +
                '}';
    }
}
