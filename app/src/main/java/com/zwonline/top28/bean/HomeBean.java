package com.zwonline.top28.bean;


import java.util.List;

/**
 * 描述：首页分类
 *
 * @author YSG
 * @date 2018/1/9
 */
public class HomeBean {

    /**
     * status : 1
     * msg :
     * data : [{"cate_id":"331","cate_name":"汽车"},{"cate_id":"330","cate_name":"公益"},{"cate_id":"323","cate_name":"环保"},{"cate_id":"322","cate_name":"家居"},{"cate_id":"321","cate_name":"美容"},{"cate_id":"320","cate_name":"服装"},{"cate_id":"319","cate_name":"餐饮"},{"cate_id":"318","cate_name":"文化"},{"cate_id":"317","cate_name":"旅游"},{"cate_id":"316","cate_name":"教育"},{"cate_id":"315","cate_name":"健康"},{"cate_id":"314","cate_name":"科技"},{"cate_id":"313","cate_name":"房产"},{"cate_id":"312","cate_name":"创业"},{"cate_id":"311","cate_name":"社会"},{"cate_id":"310","cate_name":"财经"},{"cate_id":"309","cate_name":"名人"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        public DataBean(String cate_id, String cate_name) {
            this.cate_id = cate_id;
            this.cate_name = cate_name;
        }

        public DataBean() {
        }

        public String getCate_id() {
            return cate_id;
        }

        public void setCate_id(String cate_id) {
            this.cate_id = cate_id;
        }

        public String getCate_name() {
            return cate_name;
        }

        public void setCate_name(String cate_name) {
            this.cate_name = cate_name;
        }

        /**
         * cate_id : 331
         * cate_name : 汽车
         */

        public String cate_id;
        public String cate_name;
    }
}
