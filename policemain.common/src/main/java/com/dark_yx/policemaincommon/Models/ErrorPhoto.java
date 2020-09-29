package com.dark_yx.policemaincommon.Models;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by lmp on 2016/5/12.
 */
@Table(name = "ErrorPhoto")
public class ErrorPhoto {
    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "userName")
    private String userName;

    @Column(name = "filePath")
    private String filePath;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "uploadMode")
    private String uploadMode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadMode() {
        return uploadMode;
    }

    public void setUploadMode(String uploadMode) {
        this.uploadMode = uploadMode;
    }

    @Override
    public String toString() {
        return "ErrorPhoto{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", uploadMode='" + uploadMode + '\'' +
                '}';
    }

    public ErrorPhoto(int id, String userName, String filePath, String fileName, String uploadMode) {
        this.id = id;
        this.userName = userName;
        this.filePath = filePath;
        this.fileName = fileName;
        this.uploadMode = uploadMode;
    }
    public ErrorPhoto() {
    }
}
