package com.zwonline.top28.bean;

/**
 * 公告未读数量
 */
public class NoticeNotReadCountBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"noticeNotReadCount":"2"}
     * dialog : kksh5ngsdhejqueacqtn027f20
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * noticeNotReadCount : 2
         */

        public String noticeNotReadCount;
    }
}
