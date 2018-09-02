package com.zwonline.top28.bean;

/**
 * @author YSG
 * @desc用户信息
 * @date ${Date}
 */
public class UserBean {

    /**
     * data : {"article_count":"764","contact_tel":"","did_i_follow":"1","fans_count":"4","favorite_count":"0","follow_count":"0","kefu_info":{"avatars":"/data/upload/resource/no_photo_female.png","contact_tel":"18201588900","nickname":"天亮了","uid":"112"}}
     * dialog :
     * msg :
     * status : 1
     */

    public DataBean data;
    public String dialog;
    public String msg;
    public int status;

    public static class DataBean {
        /**
         * article_count : 764
         * contact_tel :
         * did_i_follow : 1
         * fans_count : 4
         * favorite_count : 0
         * follow_count : 0
         * kefu_info : {"avatars":"/data/upload/resource/no_photo_female.png","contact_tel":"18201588900","nickname":"天亮了","uid":"112"}
         */

        public String article_count;
        public String contact_tel;
        public String did_i_follow;
        public String fans_count;
        public String favorite_count;
        public String follow_count;
        public KefuInfoBean kefu_info;

        public static class KefuInfoBean {
            /**
             * avatars : /data/upload/resource/no_photo_female.png
             * contact_tel : 18201588900
             * nickname : 天亮了
             * uid : 112
             */

            public String avatars;
            public String contact_tel;
            public String nickname;
            public String uid;
        }
    }
}
