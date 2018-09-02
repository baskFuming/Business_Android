package com.zwonline.top28.bean;

import java.util.List;

/**
 * @author YSG
 * @desc
 * @date $date$
 */
public class BusinessClassifyBean {

    /**
     * status : 1
     * msg : success
     * data : [{"c_id":"332","c_name":"餐饮小吃"},{"c_id":"333","c_name":"服装鞋帽"},{"c_id":"334","c_name":"环保机械"},{"c_id":"336","c_name":"家居建材"},{"c_id":"337","c_name":"教育网络"},{"c_id":"338","c_name":"美容保健"},{"c_id":"339","c_name":"特色项目"}]
     * dialog : ved0j4h2pafp5pfrqkl8gnjqi1
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * c_id : 332
         * c_name : 餐饮小吃
         */

        public String c_id;
        public String c_name;
    }
}
