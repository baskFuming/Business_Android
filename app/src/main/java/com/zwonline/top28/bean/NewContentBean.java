package com.zwonline.top28.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 商机圈动态列表bean
 */
public class NewContentBean implements Serializable {

    /**
     * status : 1
     * msg : 成功
     * dialog : lpkffhu8vb24vleatjurvo5a03
     */

    public int status;
    public String msg;
    public String dialog;
    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean implements Serializable {
        /**
         * user_id : 25161
         * content : 发布了文章
         * view_count : 0
         * like_count : 0
         * comment_count : 1
         * repost_id : 0
         * type : 2
         * type_target_id : 165324
         * extend_content : {"target_id":"165324","target_image":"https://sjttcdn.28.com/data/upload/article_img/1808/02/5b62b014c9d0d.jpg_150x150.jpg","target_title":"FinC重磅空投","target_description":"FinC重磅空投8月16日上线交易平台空投时间：8月1日-8月8日本轮空投总量：880万FinC加入..."}
         * is_delete : 0
         * add_date : 2018-08-02
         * add_time : 2018-08-02 15:17:41
         * update_time : 2018-08-02 15:17:41
         * is_ad : 0
         * moment_id : 133
         * comments_excerpt : [{"user_id":"224","content":"ๆไไไนำไยไบไงไบไงกวดวด","nickname":"黑桃"}]
         * author : {"avatars":"","nickname":"刀哥"}
         * did_i_follow : 0
         * did_i_like : 0
         * "original_size":{"height":"853","width":"480"},"thumb_size":{"height":"0","width":"0"}}]
         */

        public String user_id;
        public String content;
        public String view_count;
        public String like_count;
        public String comment_count;
        public String repost_id;
        public String type;
        public String type_target_id;
        public ExtendContentBean extend_content;
        public String is_delete;
        public String add_date;
        public String add_time;
        public String update_time;
        public String is_ad;
        public String moment_id;
        public AuthorBean author;

        public String getDid_i_follow() {
            return did_i_follow;
        }

        public void setDid_i_follow(String did_i_follow) {
            this.did_i_follow = did_i_follow;
        }

        public String did_i_follow;
        public String did_i_like;
        public List<CommentsExcerptBean> comments_excerpt;
        public List<ImagesArrBean> images_arr;

        public static class ExtendContentBean {
            /**
             * target_id : 165324
             * target_image : https://sjttcdn.28.com/data/upload/article_img/1808/02/5b62b014c9d0d.jpg_150x150.jpg
             * target_title : FinC重磅空投
             * target_description : FinC重磅空投8月16日上线交易平台空投时间：8月1日-8月8日本轮空投总量：880万FinC加入...
             */

            public String target_id;
            public String target_image;
            public String target_title;
            public String target_description;
        }

        public static class AuthorBean {
            /**
             * avatars :
             * nickname : 刀哥
             */

            public String avatars;
            public String nickname;
            public String identity_type;

        }

        public static class CommentsExcerptBean {
            /**
             * user_id : 224
             * content : ๆไไไนำไยไบไงไบไงกวดวด
             * nickname : 黑桃
             */

            public String user_id;
            public String content;
            public String nickname;
        }

        public static class ImagesArrBean {
            /**
             * original : https://sjttcdn.28.com/data/upload/business_circle/20180802/5b62ad8e41529.jpg
             * thumb : https://sjttcdn.28.com/data/upload/business_circle/thumb/thumb_5b62ad8e41529.jpg
             * original_size : {"height":"853","width":"480"}
             * thumb_size : {"height":"0","width":"0"}
             */

            public String original;
            public String thumb;
            public OriginalSizeBean original_size;
            public ThumbSizeBean thumb_size;

            public static class OriginalSizeBean {
                /**
                 * height : 853
                 * width : 480
                 */

                public String height;
                public String width;
            }

            public static class ThumbSizeBean {
                /**
                 * height : 0
                 * width : 0
                 */

                public String height;
                public String width;
            }
        }
    }
}
