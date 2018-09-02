package com.zwonline.top28.bean;

public class InforNoticeCleanBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"unread_count":"20"}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;


    public static class DataBean {
        /**
         * unread_count : 20
         */

        public String unread_count;

    }
}
