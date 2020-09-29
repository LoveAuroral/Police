package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by dark_yx on 2016-03-23.
 */
@Table(name = "BaasIp")
public class BassIP {
    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "Ip")
    private String Ip;

    @Column(name = "VideoIp")
    private String VideoIP;

    public String GetIp() {
        return Ip;
    }

    public void SetIP(String Ip) {
        this.Ip = Ip;
    }

    public String GetVideoIp() {
        return VideoIP;
    }

    public void setVideoIP(String videoIP) {
        this.VideoIP = videoIP;
    }


}