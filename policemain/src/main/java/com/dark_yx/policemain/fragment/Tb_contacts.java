package com.dark_yx.policemain.fragment;

/**
 * Created by Administrator on 2018/5/8.
 */

public class Tb_contacts {
    private String name;
    private String number;

    @Override
    public String toString() {
        return "Tb_contacts{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    public Tb_contacts() {

    }

    public Tb_contacts(String name, String number) {
        super();
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}



