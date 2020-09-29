package com.dark_yx.policemain.beans;

/**
 * Created by dark_yx-i on 2018/2/6.
 */

public class EntityBean<T> {
    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    private T id;

    public EntityBean(T id) {
        this.id = id;
    }

    public EntityBean() {

    }
}

