package com.zwonline.top28.bean;

import java.util.List;

/**
     * 描述：我的粉丝Bean
     * @author YSG
     * @date 2017/12/22
     */
public class MyFansBean {

    /**
     * status : 1
     * msg :
     * data : [{"followid":"115","uid":"92","last_contact_type":"1","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180207/5a7b11bc8cc72.jpg_100x100.jpg","nickname":"商机","signature":"签名","sex":"2","did_i_follow":"1","follow_url":"/App/Members/attention/uid/92.html"},{"followid":"115","uid":"1","last_contact_type":"1","avatars":"https://toutiao.28.com/data/upload/personal_avatars//171226/5a42125b1d12e.jpg_thumb.jpeg","nickname":"头条使者","signature":"创业保障 成交付费","sex":"2","did_i_follow":"1","follow_url":"/App/Members/attention/uid/1.html"},{"followid":"115","uid":"126","last_contact_type":"2","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180202/5a73cb2e33753.jpg_100x100.jpg","nickname":"海燕","signature":"為生活奔波","sex":"2","did_i_follow":"1","follow_url":"/App/Members/attention/uid/126.html"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * followid : 115
         * uid : 92
         * last_contact_type : 1
         * avatars : https://toutiao.28.com/data/upload/personal_avatars//180207/5a7b11bc8cc72.jpg_100x100.jpg
         * nickname : 商机
         * signature : 签名
         * sex : 2
         * did_i_follow : 1
         * follow_url : /App/Members/attention/uid/92.html
         */

        public String followid;
        public String uid;
        public String last_contact_type;
        public String avatars;
        public String nickname;
        public String signature;
        public String sex;
        public String did_i_follow;
        public String follow_url;
    }
}
