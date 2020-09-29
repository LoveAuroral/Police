package com.dark_yx.policemain.chat.beans;

import com.dark_yx.policemaincommon.Models.UsersBean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

@Table(name = "chatMessage")
public class ChatMessage implements Serializable {
    @Override
    public String toString() {
        return "ChatMessage{" +
                "message='" + message + '\'' +
                ", type=" + type +
                ", tickets=" + tickets +
                ", chatMessageType=" + chatMessageType +
                ", chatSendType=" + chatSendType +
                ", fromUser=" + fromUser +
                ", toUser=" + toUser +
                ", toGroupId=" + toGroupId +
                ", toUserId=" + toUserId +
                ", toGroup=" + toGroup +
                ", creationTime='" + creationTime + '\'' +
                ", creatorUserId=" + creatorUserId +
                ", id=" + id +
                ", sendState=" + sendState +
                ", sendName='" + sendName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", duration=" + duration +
                '}';
    }

    /**
     * message : 测试消息
     * tickets : 1000
     * chatMessageType : 0
     * chatSendType : 0
     * fromUser : {"name":"李官辉","userName":"liguanhui","policeNo":"p002","position":"李官辉","phoneNumber":"17685302679","id":4}
     * toUser : {"name":"李官辉","userName":"liguanhui","policeNo":"p002","position":"李官辉","phoneNumber":"17685302679","id":4}
     * toGroupId : null
     * toUserId : 4
     * toGroup : null
     * creationTime : 2018-02-06T15:40:41.947
     * creatorUserId : 4
     * id : 45
     */

    @Column(name = "message")
    private String message;

    @Column(name = "type")//发送，接收
    private int type;

    @Column(name = "tickets")
    private long tickets;

    @Column(name = "chatMessageType")
    private int chatMessageType;

    @Column(name = "chatSendType")
    private int chatSendType;

    @Column(name = "fromUser")
    private UsersBean fromUser;

    @Column(name = "toUser")
    private UsersBean toUser;

    @Column(name = "toGroupId")
    private long toGroupId;

    @Column(name = "toUserId")
    private long toUserId;

    @Column(name = "toGroup")
    private ChatGroupBean toGroup;

    @Column(name = "creationTime")
    private String creationTime;

    @Column(name = "creatorUserId")
    private int creatorUserId;

    @Column(name = "id", isId = true)
    private long id;

    @Column(name = "sendState")
    private int sendState;

    // 定义显示用户信息，简化处理
    @Column(name = "sendName")
    private String sendName;

    @Column(name = "filePath")
    private String filePath;

    @Column(name = "duration")
    private long duration;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTickets() {
        return tickets;
    }

    public void setTickets(long tickets) {
        this.tickets = tickets;
    }

    public int getChatMessageType() {
        return chatMessageType;
    }

    public void setChatMessageType(int chatMessageType) {
        this.chatMessageType = chatMessageType;
    }

    public int getChatSendType() {
        return chatSendType;
    }

    public void setChatSendType(int chatSendType) {
        this.chatSendType = chatSendType;
    }

    public UsersBean getFromUser() {
        return fromUser;
    }

    public void setFromUser(UsersBean fromUser) {
        this.fromUser = fromUser;
    }

    public UsersBean getToUser() {
        return toUser;
    }

    public void setToUser(UsersBean toUser) {
        this.toUser = toUser;
    }

    public long getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(long toGroupId) {
        this.toGroupId = toGroupId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public ChatGroupBean getToGroup() {
        return toGroup;
    }

    public void setToGroup(ChatGroupBean toGroup) {
        this.toGroup = toGroup;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public int getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(int creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public ChatMessage() {
    }
}
