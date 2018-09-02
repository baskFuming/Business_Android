package com.zwonline.top28.bean;

import java.util.List;

/**
 * 自定义标签
 */
public class RecommendTagsBean {

    public List<TagListBean> tag_list;

    public static class TagListBean {
        public TagListBean(String name, String tag_id) {
            this.name = name;
            this.tag_id = tag_id;
        }

        public TagListBean() {
        }


        /**
         * name : 情感
         * tag_id : 4
         */

        public String name;
        public String tag_id;
    }
}
