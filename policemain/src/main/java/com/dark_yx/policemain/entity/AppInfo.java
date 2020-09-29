package com.dark_yx.policemain.entity;

import android.widget.ImageView;

/**
 * Created by lmp on 2016/4/8.
 */
public class AppInfo {
    private ImageView appIcon;//图标
    private String appLabel;//app名称
    private String appPackgeName;//app包名

    public void setAppIcon(ImageView appIcon) {
        this.appIcon = appIcon;
    }

    public ImageView getAppIcon() {
        return appIcon;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppPackgeName(String appPackgeName) {
        this.appPackgeName = appPackgeName;
    }

    public String getAppPackgeName() {
        return appPackgeName;
    }

    public void AppInfo(String appLabel, String appPackgeName, ImageView appIcon) {
        this.appIcon = appIcon;//将右边的值赋给左边
        this.appLabel = appLabel;
        this.appPackgeName = appPackgeName;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
