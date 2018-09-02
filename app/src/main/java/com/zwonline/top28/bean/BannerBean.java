package com.zwonline.top28.bean;

import java.util.List;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class BannerBean {

    /**
     * status : 1
     * msg :
     * data : [{"type":"project","target":"33","img_src":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1b79b2d63ec.png_thumb.png"},{"type":"project","target":"59","img_src":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bb881a885d.png_thumb.png"},{"type":"article","target":"71","img_src":"https://toutiao.28.com/data/upload/article_img/1711/08/5a02cb3751b76.jpg"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * type : project
         * target : 33
         * img_src : https://toutiao.28.com/data/upload/personal_avatars//171127/5a1b79b2d63ec.png_thumb.png
         */

        public String type;
        public String target;
        public String img_src;
    }
}
