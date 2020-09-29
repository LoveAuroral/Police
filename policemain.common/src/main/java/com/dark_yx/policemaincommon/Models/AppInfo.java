package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Ligh on 2018/2/7 09:49
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

@Table(name = "AppInfo")
public class AppInfo {
    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", src='" + src + '\'' +
                ", version='" + version + '\'' +
                ", id=" + ids +
                '}';
    }

    /**
     * name : 123
     * packageName : 123
     * src : /FileRecords/20180205/636534418009840800.apk
     * version : 123
     * id : 2
     */

    @Column(name = "name")
    private String name;

    @Column(name = "packageName")
    private String packageName;

    @Column(name = "src")
    private String src;

    @Column(name = "version")
    private String version;

    @Column(name = "id", isId = true)
    private int ids;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getId() {
        return ids;
    }

    public void setId(int id) {
        this.ids = id;
    }

    public AppInfo() {
    }
}
