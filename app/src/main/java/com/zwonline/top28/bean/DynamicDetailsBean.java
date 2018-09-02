package com.zwonline.top28.bean;

import java.util.List;

public class DynamicDetailsBean {

    /**
     * status : 1
     * msg : 成功
     * data : [{"moment_id":"682","content":"很好的文章","add_time":"2018-08-04 10:58:03","pid":"0","ppid":"0","user_id":"97","like_count":"1","comment_count":"2","commentsExcerpt":[{"moment_id":"682","content":"嗯","add_time":"2018-08-04 11:04:09","pid":"166","ppid":"0","user_id":"97","did_i_vote":"0","comment_id":"167","member":{"user_id":"97","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180713/5b482268646ef.png_100x100.jpg","nickname":"雲焘"}},{"moment_id":"682","content":"哈哈","add_time":"2018-08-04 11:07:51","pid":"166","ppid":"167","user_id":"97","did_i_vote":"0","comment_id":"168","member":{"user_id":"97","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180713/5b482268646ef.png_100x100.jpg","nickname":"雲焘"},"pp_user":{"user_id":"97","nickname":"雲焘","content":"嗯","comment_id":"167"}}],"comment_id":"166","did_i_vote":"0","member":{"user_id":"97","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180713/5b482268646ef.png_100x100.jpg","nickname":"雲焘"}}]
     * dialog : 0i78v2dtu2vmta5398ll8dr9q4
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * moment_id : 682
         * content : 很好的文章
         * add_time : 2018-08-04 10:58:03
         * pid : 0
         * ppid : 0
         * user_id : 97
         * like_count : 1
         * comment_count : 2
         * commentsExcerpt : [{"moment_id":"682","content":"嗯","add_time":"2018-08-04 11:04:09","pid":"166","ppid":"0","user_id":"97","did_i_vote":"0","comment_id":"167","member":{"user_id":"97","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180713/5b482268646ef.png_100x100.jpg","nickname":"雲焘"}},{"moment_id":"682","content":"哈哈","add_time":"2018-08-04 11:07:51","pid":"166","ppid":"167","user_id":"97","did_i_vote":"0","comment_id":"168","member":{"user_id":"97","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180713/5b482268646ef.png_100x100.jpg","nickname":"雲焘"},"pp_user":{"user_id":"97","nickname":"雲焘","content":"嗯","comment_id":"167"}}]
         * comment_id : 166
         * did_i_vote : 0
         * member : {"user_id":"97","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180713/5b482268646ef.png_100x100.jpg","nickname":"雲焘"}
         */

        public String moment_id;
        public String content;
        public String add_time;
        public String pid;
        public String ppid;
        public String user_id;
        public String like_count;
        public String comment_count;
        public String comment_id;
        public String did_i_vote;
        public MemberBean member;
        public List<CommentsExcerptBean> commentsExcerpt;

        public static class MemberBean {
            /**
             * user_id : 97
             * avatars : https://toutiao.28.com/data/upload/personal_avatars//180713/5b482268646ef.png_100x100.jpg
             * nickname : 雲焘
             */

            public String user_id;
            public String avatars;
            public String nickname;
        }

        public static class CommentsExcerptBean {
            /**
             * moment_id : 682
             * content : 嗯
             * add_time : 2018-08-04 11:04:09
             * pid : 166
             * ppid : 0
             * user_id : 97
             * did_i_vote : 0
             * comment_id : 167
             * member : {"user_id":"97","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180713/5b482268646ef.png_100x100.jpg","nickname":"雲焘"}
             * pp_user : {"user_id":"97","nickname":"雲焘","content":"嗯","comment_id":"167"}
             */

            public String moment_id;
            public String content;
            public String add_time;
            public String pid;
            public String ppid;
            public String user_id;
            public String did_i_vote;
            public String comment_id;
            public MemberBeanX member;
            public PpUserBean pp_user;

            public static class MemberBeanX {
                /**
                 * user_id : 97
                 * avatars : https://toutiao.28.com/data/upload/personal_avatars//180713/5b482268646ef.png_100x100.jpg
                 * nickname : 雲焘
                 */

                public String user_id;
                public String avatars;
                public String nickname;
            }

            public static class PpUserBean {
                /**
                 * user_id : 97
                 * nickname : 雲焘
                 * content : 嗯
                 * comment_id : 167
                 */

                public String user_id;
                public String nickname;
                public String content;
                public String comment_id;
            }
        }
    }
}
