package com.zwonline.top28.bean;

/**
 *  新的用户推荐
 */
public class NewRecomdUserBean {


    /**
     * status : 1
     * msg : 成功
     * data : {"share_data":{"share_title":"有福一起享，有钱一起花，现在注册还不晚！","share_description":"我在商机头条看资讯日赚100+，戳我赚零花钱！","share_icon":"https://toutiao.28.com/data/upload/resource/216logo.png","share_url":"https://toutiao.28.com/Integral/getBocRedPacket?recommend_id=1"},"url":"/Home/Members/register_by_incode/incode/ZR47tRZJ.html"}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * share_data : {"share_title":"有福一起享，有钱一起花，现在注册还不晚！","share_description":"我在商机头条看资讯日赚100+，戳我赚零花钱！","share_icon":"https://toutiao.28.com/data/upload/resource/216logo.png","share_url":"https://toutiao.28.com/Integral/getBocRedPacket?recommend_id=1"}
         * url : /Home/Members/register_by_incode/incode/ZR47tRZJ.html
         */

        public ShareDataBean share_data;
        public String url;

        public static class ShareDataBean {
            /**
             * share_title : 有福一起享，有钱一起花，现在注册还不晚！
             * share_description : 我在商机头条看资讯日赚100+，戳我赚零花钱！
             * share_icon : https://toutiao.28.com/data/upload/resource/216logo.png
             * share_url : https://toutiao.28.com/Integral/getBocRedPacket?recommend_id=1
             */

            public String share_title;
            public String share_description;
            public String share_icon;
            public String share_url;

        }
    }
}
