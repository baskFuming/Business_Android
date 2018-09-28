package com.zwonline.top28.bean;

import java.util.List;

/**
 * 关注动态头列表Bean
 */
public class AtentionDynamicHeadBean {

    /**
     * data : {"list":[{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180411/5acdd2d26f323.png_100x100.jpg","did_i_follow":"1","identity_type":"1","nickname":"Tokens","uid":"163"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180802/5b6274f01a6d2.png_100x100.jpg","did_i_follow":"1","identity_type":"1","nickname":"杜红超@商机链","uid":"164"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180601/5b114523533b9.jpg_100x100.jpg","did_i_follow":"0","identity_type":"1","nickname":"NiuniuQQ","uid":"13726"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180802/5b628206e00da.png_100x100.jpg","did_i_follow":"0","identity_type":"1","nickname":"孤峰","uid":"22823"}],"show_banner":"1"}
     * dialog :
     * msg : 成功
     * status : 1
     */

    public DataBean data;
    public String dialog;
    public String msg;
    public int status;

    public static class DataBean {
        /**
         * list : [{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180411/5acdd2d26f323.png_100x100.jpg","did_i_follow":"1","identity_type":"1","nickname":"Tokens","uid":"163"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180802/5b6274f01a6d2.png_100x100.jpg","did_i_follow":"1","identity_type":"1","nickname":"杜红超@商机链","uid":"164"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180601/5b114523533b9.jpg_100x100.jpg","did_i_follow":"0","identity_type":"1","nickname":"NiuniuQQ","uid":"13726"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180802/5b628206e00da.png_100x100.jpg","did_i_follow":"0","identity_type":"1","nickname":"孤峰","uid":"22823"}]
         * show_banner : 1
         */

        public String show_banner;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * avatars : https://toutiao.28.com/data/upload/personal_avatars//180411/5acdd2d26f323.png_100x100.jpg
             * did_i_follow : 1
             * identity_type : 1
             * nickname : Tokens
             * uid : 163
             */

            public String avatars;
            public String did_i_follow;
            public String identity_type;
            public String nickname;
            public String uid;
            public String signature;
        }
    }
}
