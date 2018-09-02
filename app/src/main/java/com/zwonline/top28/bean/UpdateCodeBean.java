package com.zwonline.top28.bean;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class UpdateCodeBean {

    /**
     * status : 1
     * msg :
     * data : {"version":"2.2.1","description":"增加了一些功能\r\n更新了UI界面\r\n解决了一些BUG\r\n快来体验吧~","publish_date":"2018-03-28","force_update":"0","have_new_version":"1"}
     * dialog : sifvr0cgbu840udj5dcihs5ia0
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * version : 2.2.1
         * description : 增加了一些功能
         * 更新了UI界面
         * 解决了一些BUG
         * 快来体验吧~
         * publish_date : 2018-03-28
         * force_update : 0
         * have_new_version : 1
         */

        public String version;
        public String description;
        public String publish_date;
        public String force_update;
        public String have_new_version;
        public String package_download_url;
    }
}
