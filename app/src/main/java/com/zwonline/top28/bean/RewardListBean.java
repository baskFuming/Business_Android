package com.zwonline.top28.bean;

import java.util.List;

public class RewardListBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"total_reward_count":"4","list":[{"reward_user_id":"1","gift_id":"1","gift_count":"4","add_time":"2018-10-30 09:31:40","gift_name":"花朵","gift_img":"flower.png","reward_user_nickname":"头条使者","reward_user_avatar":"https://toutiao.28.com/data/upload/personal_avatars//171226/5a42125b1d12e.jpg_thumb.jpeg"},{"reward_user_id":"1","gift_id":"1","gift_count":"3","add_time":"2018-10-30 09:33:08","gift_name":"花朵","gift_img":"flower.png","reward_user_nickname":"头条使者","reward_user_avatar":"https://toutiao.28.com/data/upload/personal_avatars//171226/5a42125b1d12e.jpg_thumb.jpeg"},{"reward_user_id":"2","gift_id":"3","gift_count":"3","add_time":"2018-10-30 09:33:43","gift_name":"掌声","gift_img":"applause.png","reward_user_nickname":"岁月静好","reward_user_avatar":"https://toutiao.28.com/data/upload/personal_avatars//171214/5a31dd6bb4b82.jpg_thumb.jpeg"},{"reward_user_id":"3","gift_id":"4","gift_count":"2","add_time":"2018-10-30 09:34:23","gift_name":"香吻","gift_img":"kiss.png","reward_user_nickname":"一只桔","reward_user_avatar":"https://toutiao.28.com/data/upload/personal_avatars//180914/5b9b2dc0b97fa.png_100x100.jpg"}]}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * total_reward_count : 4
         * list : [{"reward_user_id":"1","gift_id":"1","gift_count":"4","add_time":"2018-10-30 09:31:40","gift_name":"花朵","gift_img":"flower.png","reward_user_nickname":"头条使者","reward_user_avatar":"https://toutiao.28.com/data/upload/personal_avatars//171226/5a42125b1d12e.jpg_thumb.jpeg"},{"reward_user_id":"1","gift_id":"1","gift_count":"3","add_time":"2018-10-30 09:33:08","gift_name":"花朵","gift_img":"flower.png","reward_user_nickname":"头条使者","reward_user_avatar":"https://toutiao.28.com/data/upload/personal_avatars//171226/5a42125b1d12e.jpg_thumb.jpeg"},{"reward_user_id":"2","gift_id":"3","gift_count":"3","add_time":"2018-10-30 09:33:43","gift_name":"掌声","gift_img":"applause.png","reward_user_nickname":"岁月静好","reward_user_avatar":"https://toutiao.28.com/data/upload/personal_avatars//171214/5a31dd6bb4b82.jpg_thumb.jpeg"},{"reward_user_id":"3","gift_id":"4","gift_count":"2","add_time":"2018-10-30 09:34:23","gift_name":"香吻","gift_img":"kiss.png","reward_user_nickname":"一只桔","reward_user_avatar":"https://toutiao.28.com/data/upload/personal_avatars//180914/5b9b2dc0b97fa.png_100x100.jpg"}]
         */

        public String total_reward_count;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * reward_user_id : 1
             * gift_id : 1
             * gift_count : 4
             * add_time : 2018-10-30 09:31:40
             * gift_name : 花朵
             * gift_img : flower.png
             * reward_user_nickname : 头条使者
             * reward_user_avatar : https://toutiao.28.com/data/upload/personal_avatars//171226/5a42125b1d12e.jpg_thumb.jpeg
             */

            public String reward_user_id;
            public String gift_id;
            public String gift_count;
            public String add_time;
            public String gift_name;
            public String gift_img;
            public String reward_user_nickname;
            public String reward_user_avatar;
        }
    }
}
