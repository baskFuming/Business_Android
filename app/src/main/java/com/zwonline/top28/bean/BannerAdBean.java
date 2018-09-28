package com.zwonline.top28.bean;

public class BannerAdBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"title":"商机头条APP","images":"https://toutiao.28.com/data/upload/shop_img/1809/27/5bac554345dd6.jpeg","sub_title":"社群运营官招募计划","jump_path":"https://toutiao.28.com","project_id":"0","is_webview":"1","is_jump_off":"0"}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * title : 商机头条APP
         * images : https://toutiao.28.com/data/upload/shop_img/1809/27/5bac554345dd6.jpeg
         * sub_title : 社群运营官招募计划
         * jump_path : https://toutiao.28.com
         * project_id : 0
         * is_webview : 1
         * is_jump_off : 0
         */

        public String title;
        public String images;
        public String sub_title;
        public String jump_path;
        public String project_id;
        public String is_webview;
        public String is_jump_off;
        public String is_show;
    }
}
