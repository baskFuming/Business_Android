package com.zwonline.top28.bean;

import java.util.List;

public class AddFollowBean {
    /**
     * status : 1
     * msg : 成功
     * data : {"list":[{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//171214/5a31dd6bb4b82.jpg_thumb.jpeg","uid":"2","nickname":"岁月静好","did_i_follow":"0"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180718/5b4eea5fbd9d4.png_100x100.jpg","uid":"3","nickname":"你算哪块小饼干","did_i_follow":"0"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180202/5a73be23ccbe7.jpg_thumb.jpeg","uid":"5","nickname":"萨瓦迪卡","did_i_follow":"0"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180706/5b3edab5c9c04.jpg_100x100.jpg","uid":"6","nickname":"你拿我咋滴","did_i_follow":"0"},{"avatars":"","uid":"7","nickname":"77","did_i_follow":"0"},{"avatars":"","uid":"8","nickname":"88","did_i_follow":"0"},{"avatars":"","uid":"9","nickname":"99","did_i_follow":"0"},{"avatars":"","uid":"10","nickname":"1010","did_i_follow":"0"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180402/5ac1ab43bb21d.jpg_100x100.jpg","uid":"16","nickname":"墨菲信息流","did_i_follow":"0"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180706/5b3f1510ad1d5.jpg_100x100.jpg","uid":"17","nickname":"桉梓龙儿","did_i_follow":"0"},{"avatars":"","uid":"34","nickname":"t2828piiq","did_i_follow":"0"},{"avatars":"","uid":"45","nickname":"t4567azra","did_i_follow":"0"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180509/5af2aa6f976f2.jpg_100x100.jpg","uid":"197","nickname":"商机头条App","did_i_follow":"0"}]}
     * dialog :
     */
    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        public List<ListBean> list;

        public static class ListBean {
            /**
             * avatars : https://toutiao.28.com/data/upload/personal_avatars//171214/5a31dd6bb4b82.jpg_thumb.jpeg
             * uid : 2
             * nickname : 岁月静好
             * did_i_follow : 0
             */
            public String avatars;
            public String uid;
            public String nickname;
            public String did_i_follow;
            public String identity_type;
            public String signature;
        }
    }
}
