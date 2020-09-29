package com.dark_yx.policemaincommon.Util;

import com.orm.SugarRecord;

/**
 * Created by Administrator on 2018/5/10.
 */

public class TokenBean extends SugarRecord {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenBean(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenBean{" +
                "token='" + token + '\'' +
                '}';
    }

}
