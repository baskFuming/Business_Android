package com.zwonline.top28.bean;

import java.util.List;

/**
 *
 *文章/动态收到的礼物列表接口
 */
public class GiftSumBean  {

    /**
     * status : 1
     * msg : 成功
     * data : {"gift_count":"12","list":[{"gift_id":"1","name":"花朵","img":"flower.png","count":"7"},{"gift_id":"2","name":"花束","img":"bouquet.png","count":"0"},{"gift_id":"3","name":"掌声","img":"applause.png","count":"3"},{"gift_id":"4","name":"香吻","img":"kiss.png","count":"2"}]}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * gift_count : 12
         * list : [{"gift_id":"1","name":"花朵","img":"flower.png","count":"7"},{"gift_id":"2","name":"花束","img":"bouquet.png","count":"0"},{"gift_id":"3","name":"掌声","img":"applause.png","count":"3"},{"gift_id":"4","name":"香吻","img":"kiss.png","count":"2"}]
         */

        public String gift_count;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * gift_id : 1
             * name : 花朵
             * img : flower.png
             * count : 7
             */

            public String gift_id;
            public String name;
            public String img;
            public String count;
        }
    }
}
