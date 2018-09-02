package com.zwonline.top28.bean;

import java.util.List;

/**
 * 描述：我的关注Bean
 * @author YSG
 * @date 2017/12/22
 */
public class MyAttentionBean {


    /**
     * status : 1
     * msg :
     * data : [{"followid":"6","uid":"97","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180116/5a5d6d4f1c40f.jpeg_thumb.jpeg","nickname":"初心不负","signature":"初心不负 方得始终","did_i_follow":"1","follow_url":"/App/Members/attention/uid/6.html"},{"followid":"112","uid":"97","avatars":"","nickname":"天亮了","signature":"努力","did_i_follow":"1","follow_url":"/App/Members/attention/uid/112.html"},{"followid":"126","uid":"97","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180104/5a4dc181bc7be.jpeg_thumb.jpeg","nickname":"海燕","signature":"为生活奔波","did_i_follow":"1","follow_url":"/App/Members/attention/uid/126.html"},{"followid":"2","uid":"97","avatars":"https://toutiao.28.com/data/upload/personal_avatars//171214/5a31dd6bb4b82.jpg_thumb.jpeg","nickname":"岁月静好","signature":"至人之用心若镜，不将不迎，应而不藏，故能胜万物而不伤。","did_i_follow":"1","follow_url":"/App/Members/attention/uid/2.html"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * followid : 6
         * uid : 97
         * avatars : https://toutiao.28.com/data/upload/personal_avatars//180116/5a5d6d4f1c40f.jpeg_thumb.jpeg
         * nickname : 初心不负
         * signature : 初心不负 方得始终
         * did_i_follow : 1
         * follow_url : /App/Members/attention/uid/6.html
         */

        public String followid;
        public String uid;
        public String avatars;
        public String nickname;
        public String signature;
        public String did_i_follow;
        public String follow_url;
    }
}
