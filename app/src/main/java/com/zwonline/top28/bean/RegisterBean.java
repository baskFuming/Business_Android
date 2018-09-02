package com.zwonline.top28.bean;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/8 16:17
 */

public class RegisterBean {

    /**
     * status : 1
     * data : {"yunxin":{"token":"云信账户token","account":"云信账户id"}}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * yunxin : {"token":"云信账户token","account":"云信账户id"}
         */

        private YunxinBean yunxin;

        public YunxinBean getYunxin() {
            return yunxin;
        }

        public void setYunxin(YunxinBean yunxin) {
            this.yunxin = yunxin;
        }

        public static class YunxinBean {
            /**
             * token : 云信账户token
             * account : 云信账户id
             */

            private String token;
            private String account;

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }
        }
    }
}
