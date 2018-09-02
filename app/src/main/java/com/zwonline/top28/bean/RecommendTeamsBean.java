package com.zwonline.top28.bean;

import java.util.List;

public class RecommendTeamsBean {

    /**
     * data : [{"current_user_count":"160","max_user_count":"200","team_avatar":"https://nos.netease.com/nim/NDQyNTI5Mw==/bmltYV8xMDg5MzAwNTU1XzE1MzAzMTE4NTA0ODFfNjE5N2Y1ZTktNzI3Mi00MjU2LTlkMDEtYmNkMmMzYjAxZmJm","team_id":"568250521","team_introduce":"","team_name":"商机头条官方群（🈲️广告）","team_notice":"官方群 568250521"},{"current_user_count":"19","max_user_count":"500","team_avatar":"https://toutiao.28.com/Application/Home/View/public/img/216logo.png","team_id":"584738401","team_introduce":"商机头条官方②群","team_name":"商机头条官方群②","team_notice":"商机头条官方②群"},{"current_user_count":"19","max_user_count":"500","team_avatar":"https://toutiao.28.com/Application/Home/View/public/img/216logo.png","team_id":"584614330","team_introduce":"商机头条官方③群","team_name":"商机头条官方群③","team_notice":"商机头条官方③群"},{"current_user_count":"17","max_user_count":"500","team_avatar":"https://toutiao.28.com/Application/Home/View/public/img/216logo.png","team_id":"584595120","team_introduce":"商机头条官方⑤群","team_name":"商机头条官方群⑤","team_notice":"商机头条官方⑤群"}]
     * dialog :
     * msg : 成功
     * status : 1
     */

    public String dialog;
    public String msg;
    public int status;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * current_user_count : 160
         * max_user_count : 200
         * team_avatar : https://nos.netease.com/nim/NDQyNTI5Mw==/bmltYV8xMDg5MzAwNTU1XzE1MzAzMTE4NTA0ODFfNjE5N2Y1ZTktNzI3Mi00MjU2LTlkMDEtYmNkMmMzYjAxZmJm
         * team_id : 568250521
         * team_introduce :
         * team_name : 商机头条官方群（🈲️广告）
         * team_notice : 官方群 568250521
         */

        public String current_user_count;
        public String max_user_count;
        public String team_avatar;
        public String team_id;
        public String team_introduce;
        public String team_name;
        public String team_notice;
        public String tag_id;//群id
        public String name;//群名称
        public String did_isChecked;
    }
}
