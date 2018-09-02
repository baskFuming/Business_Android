package com.zwonline.top28.bean;

import com.zwonline.top28.base.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sdh on 2018/3/8.
 * 我的项目实体
 */

public class ProjectBean {

    /**
     * status : 1
     * msg : 成功
     * data : [{"id":"134","uid":"97","logo":"https://toutiao.28.com/data/comp_logo/5.jpeg","logo_original":"https://toutiao.28.com/data/comp_logo/5.jpeg","enterprise_name":"柠檬工坊","enterprise_contacts":"测试","enterprise_contact_tel":"111111","enterprise_contact_address":"测试","enterprise_license":"https://toutiao.28.com/data/upload/app_pub_img/1804/16/5ad43a9bebb4a.png_150x150.jpg|https://toutiao.28.com/data/upload/app_pub_img/1804/16/5ad43aa86f863.png_150x150.jpg","check_status":"1","check_comment":"","check_time":"2018-04-16 14:14:28","add_date":"2018-04-16","add_time":"2018-04-16 14:14:28","sign":"282828","company_name":"测试","cate_id":"340","is_recommended":"0","score":"98","lang":"zh-cn","is_default":"0","weight":"0.00","level":"0"},{"id":"115","uid":"97","logo":"https://toutiao.28.com/data/comp_logo/5.jpeg","logo_original":"https://toutiao.28.com/data/comp_logo/5.jpeg","enterprise_name":"柠檬工坊","enterprise_contacts":"于先生","enterprise_contact_tel":"18513690060","enterprise_contact_address":"玉泉慧谷","enterprise_license":"https://toutiao.28.com/data/upload/app_pub_img/1803/30/5abd9bd9b0ade.png_150x150.jpg|https://toutiao.28.com/data/upload/app_pub_img/1803/30/5abd9be23106d.png_150x150.jpg","check_status":"2","check_comment":"请上传符合要求的公司资质！","check_time":"2018-03-30 10:09:09","add_date":"2018-03-30","add_time":"2018-04-10 17:56:16","sign":"888","company_name":"中网在线","cate_id":"339","is_recommended":"0","score":"98","lang":"zh-cn","is_default":"0","weight":"0.00","level":"0"},{"id":"133","uid":"97","logo":"https://toutiao.28.com/data/comp_logo/2.jpeg","logo_original":"https://toutiao.28.com/data/comp_logo/2.jpeg","enterprise_name":"柠檬工坊","enterprise_contacts":"测试","enterprise_contact_tel":"111111","enterprise_contact_address":"测试","enterprise_license":"https://toutiao.28.com/data/upload/app_pub_img/1804/16/5ad40f7ece2f7.png_150x150.jpg|https://toutiao.28.com/data/upload/app_pub_img/1804/16/5ad40f859e386.png_150x150.jpg","check_status":"2","check_comment":"谢谢","check_time":"2018-04-16 11:19:55","add_date":"2018-04-16","add_time":"2018-04-16 11:19:55","sign":"282828","company_name":"测试","cate_id":"333","is_recommended":"0","score":"98","lang":"zh-cn","is_default":"0","weight":"0.00","level":"0"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 134
         * uid : 97
         * logo : https://toutiao.28.com/data/comp_logo/5.jpeg
         * logo_original : https://toutiao.28.com/data/comp_logo/5.jpeg
         * enterprise_name : 柠檬工坊
         * enterprise_contacts : 测试
         * enterprise_contact_tel : 111111
         * enterprise_contact_address : 测试
         * enterprise_license : https://toutiao.28.com/data/upload/app_pub_img/1804/16/5ad43a9bebb4a.png_150x150.jpg|https://toutiao.28.com/data/upload/app_pub_img/1804/16/5ad43aa86f863.png_150x150.jpg
         * check_status : 1
         * check_comment :
         * check_time : 2018-04-16 14:14:28
         * add_date : 2018-04-16
         * add_time : 2018-04-16 14:14:28
         * sign : 282828
         * company_name : 测试
         * cate_id : 340
         * is_recommended : 0
         * score : 98
         * lang : zh-cn
         * is_default : 0
         * weight : 0.00
         * level : 0
         */

        public String id;
        public String uid;
        public String logo;
        public String logo_original;
        public String enterprise_name;
        public String enterprise_contacts;
        public String enterprise_contact_tel;
        public String enterprise_contact_address;
        public String enterprise_license;
        public String check_status;
        public String check_comment;
        public String check_time;
        public String add_date;
        public String add_time;
        public String sign;
        public String company_name;
        public String cate_id;
        public String is_recommended;
        public String score;
        public String lang;
        public String is_default;
        public String weight;
        public String level;
    }

}
