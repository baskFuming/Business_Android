package com.zwonline.top28.bean;

public class HongbaoPermissionBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"has_permission":"0"}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * has_permission : 0
         */

        public String has_permission;
    }
}
