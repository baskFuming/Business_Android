package com.zwonline.top28.bean;

import java.util.List;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class CompanyBean {

    /**
     * status : 1
     * msg :
     * data : {"follow_count":"7","fans_count":"4","article_count":"132","favorite_count":"2","did_i_follow":"1","contact_tel":"010-60846618-2816","post_page":[{"id":"87","enterprise_id":"85","title":"沙卡拉卡","img_path":["https://toutiao.28.com/data/upload/article_img/1803/10/5aa3524519f9d.jpg_crop.jpeg_0.jpeg","https://toutiao.28.com/data/upload/article_img/1803/10/5aa3524519f9d.jpg_crop.jpeg_1.jpeg"]},{"id":"84","enterprise_id":"64","title":"2828"}],"kefu_info":{"uid":"16","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180105/5a4f17f0ee446.jpg_thumb.jpeg","nickname":"saonian","contact_tel":"010-60846618-2816"}}
     * dialog : 7e124244bc7741d01f2beb5e1f38587a
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * follow_count : 7
         * fans_count : 4
         * article_count : 132
         * favorite_count : 2
         * did_i_follow : 1
         * contact_tel : 010-60846618-2816
         * post_page : [{"id":"87","enterprise_id":"85","title":"沙卡拉卡","img_path":["https://toutiao.28.com/data/upload/article_img/1803/10/5aa3524519f9d.jpg_crop.jpeg_0.jpeg","https://toutiao.28.com/data/upload/article_img/1803/10/5aa3524519f9d.jpg_crop.jpeg_1.jpeg"]},{"id":"84","enterprise_id":"64","title":"2828"}]
         * kefu_info : {"uid":"16","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180105/5a4f17f0ee446.jpg_thumb.jpeg","nickname":"saonian","contact_tel":"010-60846618-2816"}
         */

        public String follow_count;
        public String fans_count;
        public String article_count;
        public String favorite_count;
        public String did_i_follow;
        public String contact_tel;
        public KefuInfoBean kefu_info;
        public List<PostPageBean> post_page;

        public static class KefuInfoBean {
            /**
             * uid : 16
             * avatars : https://toutiao.28.com/data/upload/personal_avatars//180105/5a4f17f0ee446.jpg_thumb.jpeg
             * nickname : saonian
             * contact_tel : 010-60846618-2816
             */

            public String uid;
            public String avatars;
            public String nickname;
            public String contact_tel;
        }

        public static class PostPageBean {
            /**
             * id : 87
             * enterprise_id : 85
             * title : 沙卡拉卡
             * img_path : ["https://toutiao.28.com/data/upload/article_img/1803/10/5aa3524519f9d.jpg_crop.jpeg_0.jpeg","https://toutiao.28.com/data/upload/article_img/1803/10/5aa3524519f9d.jpg_crop.jpeg_1.jpeg"]
             */

            public String id;
            public String enterprise_id;
            public String title;
            public List<String> img_path;
        }
    }
}
