package com.zwonline.top28.bean;

import java.util.List;

/**
 * 发表商机圈的bean
 */
public class SendNewMomentBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"moment_id":"10","user_id":"227","content":"今天天气不错！","view_count":"0","like_count":"0","comment_count":"0","repost_id":"0","add_time":"2018-07-13 10:45:34","imagesArray":["https://toutiao.28.com/data/upload/business_circle/thumb/thumb_5b471c301c639.png","https://toutiao.28.com/data/upload/business_circle/thumb/thumb_sa8787dasdsa78d2324.png"]}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * moment_id : 10
         * user_id : 227
         * content : 今天天气不错！
         * view_count : 0
         * like_count : 0
         * comment_count : 0
         * repost_id : 0
         * add_time : 2018-07-13 10:45:34
         * imagesArray : ["https://toutiao.28.com/data/upload/business_circle/thumb/thumb_5b471c301c639.png","https://toutiao.28.com/data/upload/business_circle/thumb/thumb_sa8787dasdsa78d2324.png"]
         */

        public String moment_id;
        public String user_id;
        public String content;
        public String view_count;
        public String like_count;
        public String comment_count;
        public String repost_id;
        public String add_time;
        public List<String> imagesArray;
    }
}
