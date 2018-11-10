package com.zwonline.top28.bean;

/**
 * 用户推荐
 */
public class RecommendUserBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"title_content":"推荐用户关注商机头条公众号，并参与挖矿可获得100算力，被推荐人还可获得20-200随机算力奖励以及20-100随机商机币红包。","recommend_list_url":"https://toutiao.28.com/Members/myRecommendUserList.html","recommend_url":"https://toutiao.28.com/Boc/user.html","share_data":{"share_title":"有福一起享，有钱一起花，现在注册还不晚！","share_icon":"https://toutiao.28.com/data/upload/resource/216logo.png","share_url":"/App/Members/register_by_incode/incode/ZR47tRZJ.html"}}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * title_content : 推荐用户关注商机头条公众号，并参与挖矿可获得100算力，被推荐人还可获得20-200随机算力奖励以及20-100随机商机币红包。
         * recommend_list_url : https://toutiao.28.com/Members/myRecommendUserList.html
         * recommend_url : https://toutiao.28.com/Boc/user.html
         * share_data : {"share_title":"有福一起享，有钱一起花，现在注册还不晚！","share_icon":"https://toutiao.28.com/data/upload/resource/216logo.png","share_url":"/App/Members/register_by_incode/incode/ZR47tRZJ.html"}
         */
        public String title_content;
        public String recommend_list_url;
        public String recommend_url;
        public ShareDataBean share_data;
        public String recommend_id;
        public static class ShareDataBean {
            /**
             * share_title : 有福一起享，有钱一起花，现在注册还不晚！
             * share_icon : https://toutiao.28.com/data/upload/resource/216logo.png
             * share_url : /App/Members/register_by_incode/incode/ZR47tRZJ.html
             */
            public String share_title;
            public String share_icon;
            public String share_url;
            public String share_description;

        }
    }
}
