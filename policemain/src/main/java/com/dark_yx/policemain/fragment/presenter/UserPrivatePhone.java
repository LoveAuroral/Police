package com.dark_yx.policemain.fragment.presenter;

import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 */

public class UserPrivatePhone {


    /**
     * __abp : true
     * result : {"items":[{"id":0,"name":"11","phoneNumber":"17385878265","phoneWhiteType":0},{"id":0,"name":"qq","phoneNumber":"15086396171","phoneWhiteType":0}]}
     * success : true
     * unAuthorizedRequest : false
     */

    private boolean __abp;
    private ResultBean result;
    private boolean success;
    private boolean unAuthorizedRequest;

    public boolean is__abp() {
        return __abp;
    }

    public void set__abp(boolean __abp) {
        this.__abp = __abp;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    public void setUnAuthorizedRequest(boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }

    public static class ResultBean {
        private List<ItemsBean> items;

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * id : 0
             * name : 11
             * phoneNumber : 17385878265
             * phoneWhiteType : 0
             */

            private int id;
            private String name;
            private String phoneNumber;
            private int phoneWhiteType;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhoneNumber() {
                return phoneNumber;
            }

            public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
            }

            public int getPhoneWhiteType() {
                return phoneWhiteType;
            }

            public void setPhoneWhiteType(int phoneWhiteType) {
                this.phoneWhiteType = phoneWhiteType;
            }
        }
    }
}
