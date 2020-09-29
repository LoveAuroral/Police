package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 换班
 * Created by Lmp on 2016/6/27.
 */
@Table(name = "ChangeWork")
public class ChangeWork implements Serializable {
    @Column(name = "Id", isId = true)
    private String Id;

    @Column(name = "UserName")
    private String UserName;//发起人

    @Column(name = "UserID")
    private int UserID;

    @Column(name = "BeTimeStr")
    private String BeTimeStr;//换班时间

    @Column(name = "Status")
    private int Status;//状态状态

    @Column(name = "StatusDes")
    private String StatusDes;//状态

    @Column(name = "TimeStr")
    private String TimeStr;//值班时间

    @Column(name = "PositionName")
    private String PositionName;//值班岗位

    @Column(name = "BePositionName")
    private String BePositionName;//换班岗位

    @Column(name = "Leader")
    private String Leader;//值班领导

    @Column(name = "LeaderId")
    private int LeaderId;//值班领导id

    @Column(name = "Reason")
    private String Reason;//换班原因

    @Column(name = "IsOnDuty")
    private boolean IsOnDuty;//是否值班

    @Column(name = "isRead")
    private boolean isRead;//是否已读

    @Column(name = "UpdateTime")
    private String UpdateTime;//更新时间

    private String BeUserName;//被换班人

    private int BeUserId;

    private int PositionPbV3Id;

    private int BePositionPbV3Id;

    private int PositionV3Id;//值班岗位id

    private int BePositionV3Id;//值班岗位id

    private int PositionPbMapV3Id;

    private int BePositionPbMapV3Id;//换班岗位id

    private String DutyStartTime;//开始时间

    private String BeDutyStartTime;

    private String DutyEndTime;//结束时间

    private String BeDutyEndTime;//结束时间

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isOnDuty() {
        return IsOnDuty;
    }

    public void setOnDuty(boolean onDuty) {
        IsOnDuty = onDuty;
    }

    public String getBeDutyStartTime() {
        return BeDutyStartTime;
    }

    public void setBeDutyStartTime(String beDutyStartTime) {
        BeDutyStartTime = beDutyStartTime;
    }

    public String getBeDutyEndTime() {
        return BeDutyEndTime;
    }

    public void setBeDutyEndTime(String beDutyEndTime) {
        BeDutyEndTime = beDutyEndTime;
    }

    public int getPositionPbV3Id() {
        return PositionPbV3Id;
    }

    public void setPositionPbV3Id(int positionPbV3Id) {
        PositionPbV3Id = positionPbV3Id;
    }

    public int getBePositionPbV3Id() {
        return BePositionPbV3Id;
    }

    public void setBePositionPbV3Id(int bePositionPbV3Id) {
        BePositionPbV3Id = bePositionPbV3Id;
    }

    public int getPositionV3Id() {
        return PositionV3Id;
    }

    public void setPositionV3Id(int positionV3Id) {
        PositionV3Id = positionV3Id;
    }

    public int getBePositionV3Id() {
        return BePositionV3Id;
    }

    public void setBePositionV3Id(int bePositionV3Id) {
        BePositionV3Id = bePositionV3Id;
    }

    public String getDutyStartTime() {
        return DutyStartTime;
    }

    public void setDutyStartTime(String dutyStartTime) {
        DutyStartTime = dutyStartTime;
    }

    public String getDutyEndTime() {
        return DutyEndTime;
    }

    public void setDutyEndTime(String dutyEndTime) {
        DutyEndTime = dutyEndTime;
    }

    public String getLeader() {
        return Leader;
    }

    public void setLeader(String leader) {
        Leader = leader;
    }

    public int getLeaderId() {
        return LeaderId;
    }

    public void setLeaderId(int leaderId) {
        LeaderId = leaderId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getBeUserName() {
        return BeUserName;
    }

    public void setBeUserName(String beUserName) {
        BeUserName = beUserName;
    }

    public int getBeUserId() {
        return BeUserId;
    }

    public void setBeUserId(int beUserId) {
        BeUserId = beUserId;
    }

    public String getBeTimeStr() {
        return BeTimeStr;
    }

    public void setBeTimeStr(String beTimeStr) {
        BeTimeStr = beTimeStr;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getStatusDes() {
        return StatusDes;
    }

    public void setStatusDes(String statusDes) {
        StatusDes = statusDes;
    }

    public String getTimeStr() {
        return TimeStr;
    }

    public void setTimeStr(String timeStr) {
        TimeStr = timeStr;
    }

    public String getPositionName() {
        return PositionName;
    }

    public void setPositionName(String positionName) {
        PositionName = positionName;
    }

    public String getBePositionName() {
        return BePositionName;
    }

    public void setBePositionName(String bePositionName) {
        BePositionName = bePositionName;
    }


    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public int getPositionPbMapV3Id() {
        return PositionPbMapV3Id;
    }

    public void setPositionPbMapV3Id(int positionPbMapV3Id) {
        PositionPbMapV3Id = positionPbMapV3Id;
    }

    public int getBePositionPbMapV3Id() {
        return BePositionPbMapV3Id;
    }

    public void setBePositionPbMapV3Id(int bePositionPbMapV3Id) {
        BePositionPbMapV3Id = bePositionPbMapV3Id;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ChangeWork{" +
                ", Id=" + Id +
                ", UserName='" + UserName + '\'' +
                ", UserID=" + UserID +
                ", BeTimeStr='" + BeTimeStr + '\'' +
                ", Status=" + Status +
                ", StatusDes='" + StatusDes + '\'' +
                ", TimeStr='" + TimeStr + '\'' +
                ", PositionName='" + PositionName + '\'' +
                ", BePositionName='" + BePositionName + '\'' +
                ", Leader='" + Leader + '\'' +
                ", LeaderId=" + LeaderId +
                ", Reason='" + Reason + '\'' +
                ", IsOnDuty=" + IsOnDuty +
                ", isRead=" + isRead +
                ", UpdateTime='" + UpdateTime + '\'' +
                ", BeUserName='" + BeUserName + '\'' +
                ", BeUserId=" + BeUserId +
                ", PositionPbV3Id=" + PositionPbV3Id +
                ", BePositionPbV3Id=" + BePositionPbV3Id +
                ", PositionV3Id=" + PositionV3Id +
                ", BePositionV3Id=" + BePositionV3Id +
                ", PositionPbMapV3Id=" + PositionPbMapV3Id +
                ", BePositionPbMapV3Id=" + BePositionPbMapV3Id +
                ", DutyStartTime='" + DutyStartTime + '\'' +
                ", BeDutyStartTime='" + BeDutyStartTime + '\'' +
                ", DutyEndTime='" + DutyEndTime + '\'' +
                ", BeDutyEndTime='" + BeDutyEndTime + '\'' +
                '}';
    }
}
