package com.zwonline.top28.bean;

/**
 * 动态分享
 */
public class DynamicShareBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"share_url":"https://toutiao.28.com/Home/Index/moment_detail/id/23","share_icon":"https://toutiao.28.com/data/upload/business_circle/20180724/5b570370c82de.jpg","share_title":"傻狗子","share_description":"傻狗子"}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * share_url : https://toutiao.28.com/Home/Index/moment_detail/id/23
         * share_icon : https://toutiao.28.com/data/upload/business_circle/20180724/5b570370c82de.jpg
         * share_title : 傻狗子
         * share_description : 傻狗子
         */

        public String share_url;
        public String share_icon;
        public String share_title;
        public String share_description;
    }
}
