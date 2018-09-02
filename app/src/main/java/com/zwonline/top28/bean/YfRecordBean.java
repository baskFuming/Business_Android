package com.zwonline.top28.bean;

import java.io.Serializable;
import java.util.List;

public class YfRecordBean implements Serializable {

    /**
     * data : {"list":[{"add_time":"2018-07-03 19:48:34","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 19:47:35","amount":"0.48","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 18:05:37","amount":"0.07","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:56:18","amount":"4.06","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:54:20","amount":"0.07","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:49:24","amount":"2.02","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:48:38","amount":"1.47","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:45:41","amount":"0.09","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:35:20","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:29:45","amount":"0.06","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:21:12","amount":"0.06","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:20:19","amount":"0.06","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:18:52","amount":"1.76","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:05:33","amount":"0.50","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:03:45","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 16:35:14","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 14:41:50","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 14:24:55","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 14:09:10","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 14:02:27","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"}],"totalReceiveHongbaoAmount":"74.44"}
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
         * list : [{"add_time":"2018-07-03 19:48:34","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 19:47:35","amount":"0.48","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 18:05:37","amount":"0.07","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:56:18","amount":"4.06","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:54:20","amount":"0.07","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:49:24","amount":"2.02","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:48:38","amount":"1.47","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:45:41","amount":"0.09","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:35:20","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:29:45","amount":"0.06","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:21:12","amount":"0.06","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:20:19","amount":"0.06","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:18:52","amount":"1.76","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:05:33","amount":"0.50","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 17:03:45","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 16:35:14","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 14:41:50","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 14:24:55","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 14:09:10","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"},{"add_time":"2018-07-03 14:02:27","amount":"1.00","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘"}]
         * totalReceiveHongbaoAmount : 74.44
         */

        public String totalReceiveHongbaoAmount;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * add_time : 2018-07-03 19:48:34
             * amount : 1.00
             * avatars : https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg
             * nickname : 雲焘
             */

            public String add_time;
            public String amount;
            public String avatars;
            public String nickname;
        }
    }
}
