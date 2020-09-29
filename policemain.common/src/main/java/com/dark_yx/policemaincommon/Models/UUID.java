package com.dark_yx.policemaincommon.Models;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Ligh on 2017/2/13 16:25
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */
@Table(name = "UUID")
public class UUID {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "uniqueId")
    private String uniqueId;

    public String getID() {
        return uniqueId;
    }

    public void setID(String ID) {
        this.uniqueId = ID;
    }

    public UUID(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UUID() {
    }
}
