package com.zwonline.top28.bean;

import java.util.List;

/**
 * @author YSG
 * @desc项目认证状态
 * @date ${Date}
 */
public class EnterpriseStatusBean {

    /**
     * data : {"add_date":"2018-03-20","add_time":"2018-03-20 15:20:47","cate_id":"337","check_comment":"","check_status":"0","check_time":"0000-00-00 00:00:00","company_name":"5555","enterprise_contact_address":"玉泉慧谷","enterprise_contact_tel":"111111141","enterprise_contacts":"卢","enterprise_license":["https://toutiao.28.com/data/upload/app_pub_img/1803/20/5ab0b60e0d274.png_150x150.jpg","https://toutiao.28.com/data/upload/app_pub_img/1803/20/5ab0b64cb7195.png_150x150.jpg"],"enterprise_name":"111","id":"105","is_default":"0","is_recommended":"0","lang":"zh-cn","level":"0","logo":"https://toutiao.28.com/data/comp_logo/3.jpeg","logo_original":"https://toutiao.28.com/data/comp_logo/3.jpeg","score":"0","sign":"商机头条发发发","uid":"16","weight":"0.00"}
     * dialog :
     * msg : 成功
     * status : 1
     */

    public DataBean data;
    public String dialog;
    public String msg;
    public int status;

    public static class DataBean {
        /**
         * add_date : 2018-03-20
         * add_time : 2018-03-20 15:20:47
         * cate_id : 337
         * check_comment :
         * check_status : 0
         * check_time : 0000-00-00 00:00:00
         * company_name : 5555
         * enterprise_contact_address : 玉泉慧谷
         * enterprise_contact_tel : 111111141
         * enterprise_contacts : 卢
         * enterprise_license : ["https://toutiao.28.com/data/upload/app_pub_img/1803/20/5ab0b60e0d274.png_150x150.jpg","https://toutiao.28.com/data/upload/app_pub_img/1803/20/5ab0b64cb7195.png_150x150.jpg"]
         * enterprise_name : 111
         * id : 105
         * is_default : 0
         * is_recommended : 0
         * lang : zh-cn
         * level : 0
         * logo : https://toutiao.28.com/data/comp_logo/3.jpeg
         * logo_original : https://toutiao.28.com/data/comp_logo/3.jpeg
         * score : 0
         * sign : 商机头条发发发
         * uid : 16
         * weight : 0.00
         */

        public String add_date;
        public String add_time;
        public String cate_id;
        public String check_comment;
        public String check_status;
        public String check_time;
        public String company_name;
        public String enterprise_contact_address;
        public String enterprise_contact_tel;
        public String enterprise_contacts;
        public String enterprise_name;
        public String id;
        public String is_default;
        public String is_recommended;
        public String lang;
        public String level;
        public String logo;
        public String logo_original;
        public String score;
        public String sign;
        public String uid;
        public String weight;
        public List<String> enterprise_license;
    }
}
