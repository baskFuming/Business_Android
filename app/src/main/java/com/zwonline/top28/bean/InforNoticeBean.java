package com.zwonline.top28.bean;

import java.util.List;

public class InforNoticeBean {

    /**
     * status : 1
     * msg : 成功
     * data : [{"notification_id":"7","type":"4","url":"to_moment_comment/8064#827","subject":"https://toutiao.28.com/data/upload/business_circle/20180827/5b83cc68ab34d.jpg","subject_type":"2","content":"什么","is_read":"0","add_time":"2018-08-27 18:04:38","from_user":{"uid":"227","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180514/5af9255cbe3bb.jpg_100x100.jpg","nickname":"MightyLu"}},{"notification_id":"6","type":"3","url":"to_moment/8063","subject":"测试一下","subject_type":"1","content":"分享了您的动态","is_read":"0","add_time":"2018-08-27 18:04:24","from_user":{"uid":"227","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180514/5af9255cbe3bb.jpg_100x100.jpg","nickname":"MightyLu","identity_type":1}},{"notification_id":"5","type":"1","url":"to_moment/8063","subject":"测试一下","subject_type":"1","content":"赞了你的动态","is_read":"0","add_time":"2018-08-27 18:04:12","from_user":{"uid":"227","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180514/5af9255cbe3bb.jpg_100x100.jpg","nickname":"MightyLu","identity_type":1}},{"notification_id":"4","type":"1","url":"to_moment/8064","subject":"https://toutiao.28.com/data/upload/business_circle/20180827/5b83cc68ab34d.jpg","subject_type":"2","content":"赞了你的动态","is_read":"0","add_time":"2018-08-27 18:04:11","from_user":{"uid":"227","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180514/5af9255cbe3bb.jpg_100x100.jpg","nickname":"MightyLu","identity_type":1}}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * notification_id : 7
         * type : 4
         * url : to_moment_comment/8064#827
         * subject : https://toutiao.28.com/data/upload/business_circle/20180827/5b83cc68ab34d.jpg
         * subject_type : 2
         * content : 什么
         * is_read : 0
         * add_time : 2018-08-27 18:04:38
         * from_user : {"uid":"227","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180514/5af9255cbe3bb.jpg_100x100.jpg","nickname":"MightyLu"}
         */
        public String notification_id;
        public int type;
        public String url;
        public String subject;
        public int subject_type;
        public String content;
        public String is_read;
        public String add_time;
        public String gift_count;
        public String gift_id;
        public String gift_boc_value;
        public String gift_name;
        public String gift_img;
        public FromUserBean from_user;

        public static class FromUserBean {
            /**
             * uid : 227
             * avatars : https://toutiao.28.com/data/upload/personal_avatars//180514/5af9255cbe3bb.jpg_100x100.jpg
             * nickname : MightyLu
             */

            public String uid;
            public String avatars;
            public String nickname;
            public int identity_type;

        }
    }
}
