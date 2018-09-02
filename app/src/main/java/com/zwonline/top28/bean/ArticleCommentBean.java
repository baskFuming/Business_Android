package com.zwonline.top28.bean;

import java.util.List;

/**
 * 文章评论
 */
public class ArticleCommentBean {

    /**
     * status : 1
     * msg : 成功
     * data : [{"article_id":"101503","content":"啊啊啊啊啊啊啊啊啊啊七夕你的人生的孩子们的最大的一个人的时候我就不想再去看了一个人的时候我就不想再去看了","ctime":"2018-05-29 17:15:54","pid":"23196","ppid":"0","uid":"224","floor":"27","zan":"0","comment_count":"0","comment_id":"24732","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}},{"article_id":"101503","content":"我的人都有一个人的时候我就不想再去看了一个人的时候我就不想再去看了一个人的时候我就不","ctime":"2018-05-29 15:40:30","pid":"23196","ppid":"0","uid":"224","floor":"26","zan":"0","comment_count":"0","comment_id":"24676","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}},{"article_id":"101503","content":"//@黑桃:the only thing","ctime":"2018-05-29 14:51:22","pid":"23194","ppid":"24500","uid":"224","floor":"25","zan":"0","comment_count":"0","comment_id":"24647","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"},"pp_user":{"user_id":"224","nickname":"黑桃","content":"//@黑桃:eeee","comment_id":"24500"}},{"article_id":"101503","content":"(null)没意思","ctime":"2018-05-29 14:18:43","pid":"23145","ppid":"0","uid":"224","floor":"24","zan":"0","comment_count":"0","comment_id":"24616","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}},{"article_id":"101503","content":"//@黑桃:很好","ctime":"2018-05-29 14:17:00","pid":"23195","ppid":"24614","uid":"224","floor":"23","zan":"0","comment_count":"0","comment_id":"24615","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"},"pp_user":{"user_id":"224","nickname":"黑桃","content":"好","comment_id":"24614"}},{"article_id":"101503","content":"好","ctime":"2018-05-29 14:16:52","pid":"23195","ppid":"0","uid":"224","floor":"22","zan":"0","comment_count":"0","comment_id":"24614","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}},{"article_id":"101503","content":"(null)不错","ctime":"2018-05-29 13:50:10","pid":"23195","ppid":"0","uid":"224","floor":"21","zan":"0","comment_count":"0","comment_id":"24598","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}},{"article_id":"101503","content":"//@t006:哈哈","ctime":"2018-05-29 13:49:43","pid":"23195","ppid":"23304","uid":"224","floor":"20","zan":"0","comment_count":"0","comment_id":"24597","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"},"pp_user":{"user_id":"115","nickname":"t006","content":"mmmmmmmm","comment_id":"23304"}},{"article_id":"101503","content":"//@t006:很好","ctime":"2018-05-29 13:48:58","pid":"23143","ppid":"23145","uid":"224","floor":"19","zan":"0","comment_count":"0","comment_id":"24596","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"},"pp_user":{"user_id":"115","nickname":"t006","content":"啥意思","comment_id":"23145"}},{"article_id":"101503","content":"好","ctime":"2018-05-29 11:57:14","pid":"0","ppid":"0","uid":"224","floor":"18","zan":"0","comment_count":"0","comment_id":"24528","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}},{"article_id":"101503","content":"//@黑桃:eeee","ctime":"2018-05-29 10:45:47","pid":"23194","ppid":"23202","uid":"224","floor":"17","zan":"0","comment_count":"0","comment_id":"24500","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"},"pp_user":{"user_id":"224","nickname":"黑桃","content":"哈哈哈","comment_id":"23202"}},{"article_id":"101503","content":"//@黑桃:666666","ctime":"2018-05-29 10:39:39","pid":"23143","ppid":"23147","uid":"224","floor":"16","zan":"0","comment_count":"0","comment_id":"24484","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"},"pp_user":{"user_id":"224","nickname":"黑桃","content":"嗯？","comment_id":"23147"}},{"article_id":"101503","content":"好好","ctime":"2018-05-28 19:07:05","pid":"0","ppid":"0","uid":"224","floor":"15","zan":"0","comment_count":"0","comment_id":"23319","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}},{"article_id":"101503","content":"//@t006:nn","ctime":"2018-05-28 19:04:43","pid":"23147","ppid":"23195","uid":"224","floor":"14","zan":"0","comment_count":"0","comment_id":"23315","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"},"pp_user":{"user_id":"115","nickname":"t006","content":"。。。。。。","comment_id":"23195"}},{"article_id":"101503","content":"//@t006:------","ctime":"2018-05-28 19:02:40","pid":"23147","ppid":"23191","uid":"224","floor":"13","zan":"1","comment_count":"0","comment_id":"23313","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"},"pp_user":{"user_id":"115","nickname":"t006","content":"==","comment_id":"23191"}},{"article_id":"101503","content":"//@t006:the new update is","ctime":"2018-05-28 18:57:36","pid":"23195","ppid":"23304","uid":"224","floor":"12","zan":"0","comment_count":"0","comment_id":"23308","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"},"pp_user":{"user_id":"115","nickname":"t006","content":"mmmmmmmm","comment_id":"23304"}},{"article_id":"101503","content":"mmmmmmmm","ctime":"2018-05-28 18:56:10","pid":"23195","ppid":"0","uid":"115","floor":"11","zan":"0","comment_count":"0","comment_id":"23304","did_i_vote":"0","member":{"user_id":"115","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180515/5afa34feb3088.png_100x100.jpg","nickname":"t006"}},{"article_id":"101503","content":"哈哈哈","ctime":"2018-05-28 16:42:07","pid":"23194","ppid":"0","uid":"224","floor":"10","zan":"1","comment_count":"0","comment_id":"23202","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}},{"article_id":"101503","content":"呵呵。。","ctime":"2018-05-28 16:38:56","pid":"23194","ppid":"0","uid":"224","floor":"9","zan":"1","comment_count":"2","commentsExcerpt":[{"article_id":"101503","content":"我的人都有一个人的时候我就不想再去看了一个人的时候我就不想再去看了一个人的时候我就不","ctime":"2018-05-29 15:40:30","pid":"23196","ppid":"0","uid":"224","floor":"26","zan":"0","did_i_vote":"0","comment_id":"24676","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}},{"article_id":"101503","content":"啊啊啊啊啊啊啊啊啊啊七夕你的人生的孩子们的最大的一个人的时候我就不想再去看了一个人的时候我就不想再去看了","ctime":"2018-05-29 17:15:54","pid":"23196","ppid":"0","uid":"224","floor":"27","zan":"0","did_i_vote":"0","comment_id":"24732","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}}],"comment_id":"23196","did_i_vote":"0","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}},{"article_id":"101503","content":"。。。。。。","ctime":"2018-05-28 16:35:04","pid":"23147","ppid":"0","uid":"115","floor":"8","zan":"1","comment_count":"6","commentsExcerpt":[{"article_id":"101503","content":"mmmmmmmm","ctime":"2018-05-28 18:56:10","pid":"23195","ppid":"0","uid":"115","floor":"11","zan":"0","did_i_vote":"0","comment_id":"23304","member":{"user_id":"115","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180515/5afa34feb3088.png_100x100.jpg","nickname":"t006"}},{"article_id":"101503","content":"//@t006:thenewupdateis","ctime":"2018-05-28 18:57:36","pid":"23195","ppid":"23304","uid":"224","floor":"12","zan":"0","did_i_vote":"0","comment_id":"23308","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}}],"comment_id":"23195","did_i_vote":"0","member":{"user_id":"115","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180515/5afa34feb3088.png_100x100.jpg","nickname":"t006"}}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * article_id : 101503
         * content : 啊啊啊啊啊啊啊啊啊啊七夕你的人生的孩子们的最大的一个人的时候我就不想再去看了一个人的时候我就不想再去看了
         * ctime : 2018-05-29 17:15:54
         * pid : 23196
         * ppid : 0
         * uid : 224
         * floor : 27
         * zan : 0
         * comment_count : 0
         * comment_id : 24732
         * did_i_vote : 0
         * member : {"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}
         * pp_user : {"user_id":"224","nickname":"黑桃","content":"//@黑桃:eeee","comment_id":"24500"}
         * commentsExcerpt : [{"article_id":"101503","content":"我的人都有一个人的时候我就不想再去看了一个人的时候我就不想再去看了一个人的时候我就不","ctime":"2018-05-29 15:40:30","pid":"23196","ppid":"0","uid":"224","floor":"26","zan":"0","did_i_vote":"0","comment_id":"24676","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}},{"article_id":"101503","content":"啊啊啊啊啊啊啊啊啊啊七夕你的人生的孩子们的最大的一个人的时候我就不想再去看了一个人的时候我就不想再去看了","ctime":"2018-05-29 17:15:54","pid":"23196","ppid":"0","uid":"224","floor":"27","zan":"0","did_i_vote":"0","comment_id":"24732","member":{"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}}]
         */

        public String article_id;
        public String content;
        public String ctime;
        public String pid;
        public String ppid;
        public String uid;
        public String floor;
        public String zan;
        public String comment_count;
        public String comment_id;
        public String did_i_vote;
        public MemberBean member;
        public PpUserBean pp_user;
        public List<CommentsExcerptBean> commentsExcerpt;

        public static class MemberBean {
            /**
             * user_id : 224
             * avatars : https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg
             * nickname : 黑桃
             */

            public String user_id;
            public String avatars;
            public String nickname;
        }

        public static class PpUserBean {
            /**
             * user_id : 224
             * nickname : 黑桃
             * content : //@黑桃:eeee
             * comment_id : 24500
             */

            public String user_id;
            public String nickname;
            public String content;
            public String comment_id;
        }

        public static class CommentsExcerptBean {
            /**
             * article_id : 101503
             * content : 我的人都有一个人的时候我就不想再去看了一个人的时候我就不想再去看了一个人的时候我就不
             * ctime : 2018-05-29 15:40:30
             * pid : 23196
             * ppid : 0
             * uid : 224
             * floor : 26
             * zan : 0
             * did_i_vote : 0
             * comment_id : 24676
             * member : {"user_id":"224","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg","nickname":"黑桃"}
             */

            public String article_id;
            public String content;
            public String ctime;
            public String pid;
            public String ppid;
            public String uid;
            public String floor;
            public String zan;
            public String did_i_vote;
            public String comment_id;
            public MemberBeanX member;
            public PpUserBean pp_user;
            public static class MemberBeanX {
                /**
                 * user_id : 224
                 * avatars : https://toutiao.28.com/data/upload/personal_avatars//180516/5afb85db3dd0f.jpg_100x100.jpg
                 * nickname : 黑桃
                 */

                public String user_id;
                public String avatars;
                public String nickname;
            }
            public static class PpUserBean {
                /**
                 * user_id : 224
                 * nickname : 黑桃
                 * content : 你好
                 * comment_id : 24857
                 */

                public String user_id;
                public String nickname;
                public String content;
                public String comment_id;
            }
        }
    }
}
