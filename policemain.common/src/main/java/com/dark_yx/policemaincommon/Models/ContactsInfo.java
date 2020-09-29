package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by lmp on 2016/5/30.
 */
@Table(name = "ContactsInfo")
public class ContactsInfo {
    @Column(name = "id")
    private int id;
    @Column(name = "jid", isId = true)
    private String jid;

    public int getId() {
        return id;
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
        return "ContactsInfo{" +
                "id=" + id +
                ", jid='" + jid + '\'' +
                '}';
    }
}
