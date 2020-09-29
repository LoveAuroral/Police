package com.dark_yx.uploadlib.beans;

import java.util.List;

/**
 * Created by dark_yx-i on 2018/2/26.
 */

public class DataDicBean {

    @Override
    public String toString() {
        return "DataDicBean{" +
                "result=" + result +
                ", targetUrl=" + targetUrl +
                ", success=" + success +
                ", error=" + error +
                ", unAuthorizedRequest=" + unAuthorizedRequest +
                ", __abp=" + __abp +
                '}';
    }

    /**
     * result : {"items":[{"displayName":"办公区","value":"办公区","number":null,"parentNo":null,"extend":null,"dataDictionaryId":2,"id":6}]}
     * targetUrl : null
     * success : true
     * error : null
     * unAuthorizedRequest : false
     * __abp : true
     */

    private ResultBean result;
    private Object targetUrl;
    private boolean success;
    private Object error;
    private boolean unAuthorizedRequest;
    private boolean __abp;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public Object getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(Object targetUrl) {
        this.targetUrl = targetUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public boolean isUnAuthorizedRequest() {
        return unAuthorizedRequest;
    }

    public void setUnAuthorizedRequest(boolean unAuthorizedRequest) {
        this.unAuthorizedRequest = unAuthorizedRequest;
    }

    public boolean is__abp() {
        return __abp;
    }

    public void set__abp(boolean __abp) {
        this.__abp = __abp;
    }

    public static class ResultBean {
        @Override
        public String toString() {
            return "ResultBean{" +
                    "items=" + items +
                    '}';
        }

        private List<ItemsBean> items;

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {

            @Override
            public String toString() {
                return "ItemsBean{" +
                        "displayName='" + displayName + '\'' +
                        ", value='" + value + '\'' +
                        ", number=" + number +
                        ", parentNo=" + parentNo +
                        ", extend=" + extend +
                        ", dataDictionaryId=" + dataDictionaryId +
                        ", id=" + id +
                        '}';
            }

            /**
             * displayName : 办公区
             * value : 办公区
             * number : null
             * parentNo : null
             * extend : null
             * dataDictionaryId : 2
             * id : 6
             */

            private String displayName;
            private String value;
            private Object number;
            private Object parentNo;
            private Object extend;
            private int dataDictionaryId;
            private int id;

            public String getDisplayName() {
                return displayName;
            }

            public void setDisplayName(String displayName) {
                this.displayName = displayName;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public Object getNumber() {
                return number;
            }

            public void setNumber(Object number) {
                this.number = number;
            }

            public Object getParentNo() {
                return parentNo;
            }

            public void setParentNo(Object parentNo) {
                this.parentNo = parentNo;
            }

            public Object getExtend() {
                return extend;
            }

            public void setExtend(Object extend) {
                this.extend = extend;
            }

            public int getDataDictionaryId() {
                return dataDictionaryId;
            }

            public void setDataDictionaryId(int dataDictionaryId) {
                this.dataDictionaryId = dataDictionaryId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
