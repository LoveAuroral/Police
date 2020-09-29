package com.dark_yx.policemaincommon.Models;

import java.util.List;

/**
 * Created by Ligh on 2018/2/4 17:30
 * 邮箱:1256144200@qq.com
 * 打开电脑我们如此接近,关上电脑我们那么遥远
 */

public class TreeBean {


    private List<DepartMentContentBean> DepartMentContent;
    private List<PhoneContactsPersonalBean> PhoneContactsPersonal;

    public List<DepartMentContentBean> getDepartMentContent() {
        return DepartMentContent;
    }

    public void setDepartMentContent(List<DepartMentContentBean> DepartMentContent) {
        this.DepartMentContent = DepartMentContent;
    }

    public List<PhoneContactsPersonalBean> getPhoneContactsPersonal() {
        return PhoneContactsPersonal;
    }

    public void setPhoneContactsPersonal(List<PhoneContactsPersonalBean> PhoneContactsPersonal) {
        this.PhoneContactsPersonal = PhoneContactsPersonal;
    }

    public static class DepartMentContentBean {
        /**
         * DepartmentGroupId : 2
         * DepartmentGroupName : 银川市男子强制戒毒所
         * DepartmentGroupParentId : 0
         * DepartMentContents : [{"DepartmentGroupId":4,"DepartmentGroupName":"所领导","DepartMentUser":[{"UserId":220,"UserName":"bz","UserPhone":"123214","DepartMentId":4},{"UserId":219,"UserName":"zz","UserPhone":"234234","DepartMentId":4},{"UserId":6,"UserName":"admin","UserPhone":"654321","DepartMentId":4}],"DepartmentParentId":2},{"DepartmentGroupId":5,"DepartmentGroupName":"调研室","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":6,"DepartmentGroupName":"工会","DepartMentUser":[{"UserId":221,"UserName":"as","UserPhone":"123234","DepartMentId":6}],"DepartmentParentId":2},{"DepartmentGroupId":7,"DepartmentGroupName":"办公室","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":8,"DepartmentGroupName":"政治处","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":9,"DepartmentGroupName":"财务装备科","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":10,"DepartmentGroupName":"督查室","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":11,"DepartmentGroupName":"纪检监察室","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":12,"DepartmentGroupName":"戒毒管理科","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":13,"DepartmentGroupName":"生卫科","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":14,"DepartmentGroupName":"基建办","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":15,"DepartmentGroupName":"教育科","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":16,"DepartmentGroupName":"监控信息中心","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":17,"DepartmentGroupName":"探访中心","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":18,"DepartmentGroupName":"卫生所","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":19,"DepartmentGroupName":"一大队","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":20,"DepartmentGroupName":"二大队","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":21,"DepartmentGroupName":"三大队","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":22,"DepartmentGroupName":"护卫队","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":23,"DepartmentGroupName":"收治大队","DepartMentUser":[],"DepartmentParentId":2},{"DepartmentGroupId":24,"DepartmentGroupName":"入所大队","DepartMentUser":[],"DepartmentParentId":2}]
         */

        private int DepartmentGroupId;
        private String DepartmentGroupName;
        private int DepartmentGroupParentId;
        private List<DepartMentContentsBean> DepartMentContents;

        public int getDepartmentGroupId() {
            return DepartmentGroupId;
        }

        public void setDepartmentGroupId(int DepartmentGroupId) {
            this.DepartmentGroupId = DepartmentGroupId;
        }

        public String getDepartmentGroupName() {
            return DepartmentGroupName;
        }

        public void setDepartmentGroupName(String DepartmentGroupName) {
            this.DepartmentGroupName = DepartmentGroupName;
        }

        public int getDepartmentGroupParentId() {
            return DepartmentGroupParentId;
        }

        public void setDepartmentGroupParentId(int DepartmentGroupParentId) {
            this.DepartmentGroupParentId = DepartmentGroupParentId;
        }

        public List<DepartMentContentsBean> getDepartMentContents() {
            return DepartMentContents;
        }

        public void setDepartMentContents(List<DepartMentContentsBean> DepartMentContents) {
            this.DepartMentContents = DepartMentContents;
        }

        public static class DepartMentContentsBean {
            /**
             * DepartmentGroupId : 4
             * DepartmentGroupName : 所领导
             * DepartMentUser : [{"UserId":220,"UserName":"bz","UserPhone":"123214","DepartMentId":4},{"UserId":219,"UserName":"zz","UserPhone":"234234","DepartMentId":4},{"UserId":6,"UserName":"admin","UserPhone":"654321","DepartMentId":4}]
             * DepartmentParentId : 2
             */

            private int DepartmentGroupId;
            private String DepartmentGroupName;
            private int DepartmentParentId;
            private List<DepartMentUserBean> DepartMentUser;

            public int getDepartmentGroupId() {
                return DepartmentGroupId;
            }

            public void setDepartmentGroupId(int DepartmentGroupId) {
                this.DepartmentGroupId = DepartmentGroupId;
            }

            public String getDepartmentGroupName() {
                return DepartmentGroupName;
            }

            public void setDepartmentGroupName(String DepartmentGroupName) {
                this.DepartmentGroupName = DepartmentGroupName;
            }

            public int getDepartmentParentId() {
                return DepartmentParentId;
            }

            public void setDepartmentParentId(int DepartmentParentId) {
                this.DepartmentParentId = DepartmentParentId;
            }

            public List<DepartMentUserBean> getDepartMentUser() {
                return DepartMentUser;
            }

            public void setDepartMentUser(List<DepartMentUserBean> DepartMentUser) {
                this.DepartMentUser = DepartMentUser;
            }

            public static class DepartMentUserBean {
                /**
                 * UserId : 220
                 * UserName : bz
                 * UserPhone : 123214
                 * DepartMentId : 4
                 */

                private int UserId;
                private String UserName;
                private String UserPhone;
                private int DepartMentId;

                public int getUserId() {
                    return UserId;
                }

                public void setUserId(int UserId) {
                    this.UserId = UserId;
                }

                public String getUserName() {
                    return UserName;
                }

                public void setUserName(String UserName) {
                    this.UserName = UserName;
                }

                public String getUserPhone() {
                    return UserPhone;
                }

                public void setUserPhone(String UserPhone) {
                    this.UserPhone = UserPhone;
                }

                public int getDepartMentId() {
                    return DepartMentId;
                }

                public void setDepartMentId(int DepartMentId) {
                    this.DepartMentId = DepartMentId;
                }
            }
        }
    }

    public static class PhoneContactsPersonalBean {
        /**
         * ID : 10
         * UserName : 罗哥
         * PhoneNumber : 13590372650
         * UserId : 6
         * UserNames : admin
         * Type : null
         */

        private int ID;
        private String UserName;
        private String PhoneNumber;
        private String UserId;
        private String UserNames;
        private Object Type;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getPhoneNumber() {
            return PhoneNumber;
        }

        public void setPhoneNumber(String PhoneNumber) {
            this.PhoneNumber = PhoneNumber;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public String getUserNames() {
            return UserNames;
        }

        public void setUserNames(String UserNames) {
            this.UserNames = UserNames;
        }

        public Object getType() {
            return Type;
        }

        public void setType(Object Type) {
            this.Type = Type;
        }
    }
}
