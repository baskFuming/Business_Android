package com.zwonline.top28.bean;

import java.util.List;

/**
 * 礼物接口
 */
public class GiftBean {

    /**
     * status : 1
     * msg : 成功
     * data : [{"gift_id":"1","name":"花朵","img":"flower.png","value":"2"},{"gift_id":"2","name":"花束","img":"bouquet.png","value":"15"},{"gift_id":"3","name":"掌声","img":"applause.png","value":"1"},{"gift_id":"4","name":"香吻","img":"kiss.png","value":"10"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * gift_id : 1
         * name : 花朵
         * img : flower.png
         * value : 2
         */

        public String gift_id;
        public String name;
        public String img;
        public String value;
    }
}
