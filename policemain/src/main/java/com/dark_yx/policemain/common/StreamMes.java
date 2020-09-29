package com.dark_yx.policemain.common;

import com.intel.webrtc.base.RemoteStream;

/**
 * Created by songwei on 2016-04-07.
 */
public class StreamMes {

    public String peerID;
    public String type;
    public RemoteStream stream;
    public long firstClickTime;

    @Override
    public String toString() {
        return "StreamMes{" +
                "peerID='" + peerID + '\'' +
                ", type='" + type + '\'' +
                ", stream=" + stream +
                ", firstClickTime=" + firstClickTime +
                '}';
    }

    public void setType(String type) {
        this.type = type;
    }
}
