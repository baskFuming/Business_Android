package com.zwonline.top28.bean;

public class PersonageInfoBean {

    /**
     * status : 1
     * msg :
     * data : {"follow_count":"10","fans_count":"87","article_count":"19745","favorite_count":"12","did_i_follow":"1","avatar":"https://toutiao.28.com/data/upload/personal_avatars//180402/5ac1ab43bb21d.jpg_100x100.jpg","nickname":"墨菲信息流"}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * follow_count : 10
         * fans_count : 87
         * article_count : 19745
         * favorite_count : 12
         * did_i_follow : 1
         * avatar : https://toutiao.28.com/data/upload/personal_avatars//180402/5ac1ab43bb21d.jpg_100x100.jpg
         * nickname : 墨菲信息流
         */

        public String follow_count;
        public String fans_count;
        public String article_count;
        public String favorite_count;
        public String did_i_follow;
        public String avatar;
        public String nickname;
        public String signature;
        public String identity_type;
    }
}
