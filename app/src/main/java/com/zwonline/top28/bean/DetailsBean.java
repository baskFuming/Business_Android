package com.zwonline.top28.bean;

import java.util.List;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class DetailsBean {



    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {

        public VoBean vo;
        public MembersBean members;
        public MembersInfoBean members_info;
        public CompanyBeanX company;
        public String is_attention;
        public String comment_url;

        public static class VoBean {


            public String id;
            public String title;
            public String content;
            public String uid;
            public String cate_pid;
            public String view;
            public String reply;
            public String showtime;
        }

        public static class MembersBean {
            /**
             * uid : 5
             * username : t5720xdfe
             * avatars : https://toutiao.28.com/data/upload/personal_avatars//171201/5a20b6f46c233.jpg_thumb.jpeg
             */

            public String uid;
            public String username;
            public String avatars;
        }

        public static class MembersInfoBean {
            /**
             * uid : 5
             * nickname : 萨瓦迪卡
             * realname : 佩琪
             * phone : 13601115720
             * age : 34
             * sex_cn : 男士
             * signature : 有花堪折直须折，莫待无花空折枝！
             * residence : 北京海淀
             * fans : 9
             * follow : 8
             * favorite : 2
             * publish : 906
             * share : 0
             * cate_pid : 317
             * avatar : https://toutiao.28.com/data/upload/personal_avatars//171201/5a20b6f46c233.jpg_thumb.jpeg
             * username : t5720xdfe
             * is_enterprise_auth_user : 0
             * company : {"id":"1","uid":"1","enterprise_name":"商机头条","enterprise_contact_tel":"010-60846618-2816","logo":"https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png","sign":"创业保险 签约付费","company_name":[],"cate_id":"337"}
             */

            public String uid;
            public String nickname;
            public String realname;
            public String phone;
            public String age;
            public String sex_cn;
            public String signature;
            public String residence;
            public String fans;
            public String follow;
            public String favorite;
            public String publish;
            public String share;
            public String cate_pid;
            public String avatar;
            public String username;
            public String is_enterprise_auth_user;
            public CompanyBean company;

            public static class CompanyBean {
                /**
                 * id : 1
                 * uid : 1
                 * enterprise_name : 商机头条
                 * enterprise_contact_tel : 010-60846618-2816
                 * logo : https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png
                 * sign : 创业保险 签约付费
                 * company_name : []
                 * cate_id : 337
                 */

                public String id;
                public String uid;
                public String enterprise_name;
                public String enterprise_contact_tel;
                public String logo;
                public String sign;
                public String cate_id;
                public List<?> company_name;
            }
        }

        public static class CompanyBeanX {
            /**
             * id : 1
             * uid : 1
             * enterprise_name : 商机头条
             * enterprise_contact_tel : 010-60846618-2816
             * logo : https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png
             * sign : 创业保险 签约付费
             * company_name : []
             * cate_id : 337
             */

            public String id;
            public String uid;
            public String enterprise_name;
            public String enterprise_contact_tel;
            public String logo;
            public String sign;
            public String cate_id;
            public List<?> company_name;
        }
    }
}
