package com.dark_yx.policemain.chat.beans;

/**
 * Created by Ligh on 2018/3/1 15:13
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class NotificationBeans {
    public static final String TYPE_NOTICE = "Announcement.Send";
    public static final String TYPE_DEVICE = "Device.Manage";
    public static final String TYPE_CAMERA_CHANGE = "CameraChange";
    public static final String ITEM_REBOOT = "reboot";
    public static final String ITEM_SHUTDOWN = "shutdown";
    public static final String ITEM_FORMAT = "format";
    public static final String ITEM_UNLOCK = "unlock";
    public static final String ITEM_WIFIDISABLE = "wifiDisable";
    public static final String ITEM_BLUETOOTHDISABLE = "bluetoothDisable";
    public static final String ITEM_BLUETOOTHABLE = "BluetoothAble";
    public static final String ITEM_LOCK = "lock";
    public static final String SETMICROPHONEDISABLED = "setMicrophoneDisabled";//禁用录音
    public static final String SETMICROPHONEABLED = "setMicrophoneAbled";//启用录音
    public static final String SETVIDEODISABLED = "setVideoDisabled";//禁用录像
    public static final String SETVIDEOABLED = "setVideoAbled";//启用录像

    /**
     * tenantId : 1
     * userId : 10
     * state : 0
     * notification : {"tenantId":1,"notificationName":"Device.Manage","data":{"message":"reboot","type":"Abp.Notifications.MessageNotificationData","properties":{"message":"reboot","data":{"User":{"Name":"杜甫","UserName":"dufu","PoliceNo":"12345","Position":null,"PhoneNumber":"18785166716","Id":10},"Imei":"864751035695173","No":"00005","IsOnline":false,"AppVersion":"3.0.9","SystemVersion":"7.0","Id":12}}},"entityType":null,"entityTypeName":null,"entityId":null,"severity":0,"creationTime":"2018-03-01T15:10:41.4410567+08:00","id":"12c7ef0e-49cc-4d0c-8b2f-d5779ada1da2"}
     * id : facffdd6-771b-469a-ba6d-60688a673ace
     */

    private int tenantId;
    private int userId;
    private int state;
    private NotificationBean notification;
    private String id;

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public NotificationBean getNotification() {
        return notification;
    }

    public void setNotification(NotificationBean notification) {
        this.notification = notification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class NotificationBean {
        /**
         * tenantId : 1
         * notificationName : Device.Manage
         * data : {"message":"reboot","type":"Abp.Notifications.MessageNotificationData","properties":{"message":"reboot","data":{"User":{"Name":"杜甫","UserName":"dufu","PoliceNo":"12345","Position":null,"PhoneNumber":"18785166716","Id":10},"Imei":"864751035695173","No":"00005","IsOnline":false,"AppVersion":"3.0.9","SystemVersion":"7.0","Id":12}}}
         * entityType : null
         * entityTypeName : null
         * entityId : null
         * severity : 0
         * creationTime : 2018-03-01T15:10:41.4410567+08:00
         * id : 12c7ef0e-49cc-4d0c-8b2f-d5779ada1da2
         */

        private int tenantId;
        private String notificationName;
        private DataBeanX data;
        private String entityType;
        private String entityTypeName;
        private String entityId;
        private int severity;
        private String creationTime;
        private String id;

        public int getTenantId() {
            return tenantId;
        }

        public void setTenantId(int tenantId) {
            this.tenantId = tenantId;
        }

        public String getNotificationName() {
            return notificationName;
        }

        public void setNotificationName(String notificationName) {
            this.notificationName = notificationName;
        }

        public DataBeanX getData() {
            return data;
        }

        public void setData(DataBeanX data) {
            this.data = data;
        }

        public String getEntityType() {
            return entityType;
        }

        public void setEntityType(String entityType) {
            this.entityType = entityType;
        }

        public String getEntityTypeName() {
            return entityTypeName;
        }

        public void setEntityTypeName(String entityTypeName) {
            this.entityTypeName = entityTypeName;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public int getSeverity() {
            return severity;
        }

        public void setSeverity(int severity) {
            this.severity = severity;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(String creationTime) {
            this.creationTime = creationTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static class DataBeanX {
            /**
             * message : reboot
             * type : Abp.Notifications.MessageNotificationData
             * properties : {"message":"reboot","data":{"User":{"Name":"杜甫","UserName":"dufu","PoliceNo":"12345","Position":null,"PhoneNumber":"18785166716","Id":10},"Imei":"864751035695173","No":"00005","IsOnline":false,"AppVersion":"3.0.9","SystemVersion":"7.0","Id":12}}
             */

            private String message;
            private String type;

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }


        }
    }
}
