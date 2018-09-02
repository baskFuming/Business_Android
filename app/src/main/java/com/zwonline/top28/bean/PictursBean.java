package com.zwonline.top28.bean;

import java.util.List;

public class PictursBean {

    /**
     * status : 1
     * msg :
     * data : [{"name":"error.png","type":"image/png","size":720817,"hash":"3aab7ca8c483c547fce5a78826b7d6af","original_save_url":"/data/upload/business_circle/20180712/5b4728ffc9cab.png","original_file_size":{"width":3122,"height":1536},"thumb_save_url":"/data/upload/business_circle/thumb/thumb_5b4728ffc9cab.png","thumb_file_size":{"width":345,"height":169}}]
     */

    public int status;
    public String msg;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * name : error.png
         * type : image/png
         * size : 720817
         * hash : 3aab7ca8c483c547fce5a78826b7d6af
         * original_save_url : /data/upload/business_circle/20180712/5b4728ffc9cab.png
         * original_file_size : {"width":3122,"height":1536}
         * thumb_save_url : /data/upload/business_circle/thumb/thumb_5b4728ffc9cab.png
         * thumb_file_size : {"width":345,"height":169}
         */

        public String name;
        public String type;
        public int size;
        public String hash;
        public String original_save_url;
        public OriginalFileSizeBean original_file_size;
        public String thumb_save_url;
        public ThumbFileSizeBean thumb_file_size;

        public static class OriginalFileSizeBean {
            /**
             * width : 3122
             * height : 1536
             */

            public int width;
            public int height;
        }

        public static class ThumbFileSizeBean {
            /**
             * width : 345
             * height : 169
             */

            public int width;
            public int height;
        }
    }

}
