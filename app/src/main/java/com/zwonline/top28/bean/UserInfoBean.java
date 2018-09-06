package com.zwonline.top28.bean;

/**
 * @author YSG
 * @desc获取个人信息
 * @date ${Date}
 */
public class UserInfoBean {

    /**
     * status : 1
     * msg : success
     * data : {"user":{"uid":"6","nickname":"初心不负","realname":"","phone":"13810302297","age":"28","sex_cn":"男士","signature":"初心不负 方得始终","residence":"北京市丰台区","fans":"4","follow":"9","favorite":"17","publish":"0","share":"0","cate_pid":"320","avatar":"https://toutiao.28.com/data/upload/personal_avatars//180116/5a5d6d4f1c40f.jpeg_thumb.jpeg","utype":"2","username":"t2297sgdr","is_enterprise_auth_user":"0","company":{"id":"1","uid":"1","enterprise_name":"商机头条","logo":"https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png","sign":"创业保险 签约付费","company_name":"","cate_id":"337"}},"yunxin":{"token":"39728f29edc3d9d87c683c5c23f2e5e1","account":"6"}}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;
    public static class DataBean {
        /**
         * user : {"uid":"6","nickname":"初心不负","realname":"","phone":"13810302297","age":"28","sex_cn":"男士","signature":"初心不负 方得始终","residence":"北京市丰台区","fans":"4","follow":"9","favorite":"17","publish":"0","share":"0","cate_pid":"320","avatar":"https://toutiao.28.com/data/upload/personal_avatars//180116/5a5d6d4f1c40f.jpeg_thumb.jpeg","utype":"2","username":"t2297sgdr","is_enterprise_auth_user":"0","company":{"id":"1","uid":"1","enterprise_name":"商机头条","logo":"https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png","sign":"创业保险 签约付费","company_name":"","cate_id":"337"}}
         * yunxin : {"token":"39728f29edc3d9d87c683c5c23f2e5e1","account":"6"}
         */

        public UserBean user;
        public YunxinBean yunxin;

        public static class UserBean {
            /**
             * uid : 6
             * nickname : 初心不负
             * realname :
             * phone : 13810302297
             * age : 28
             * sex_cn : 男士
             * signature : 初心不负 方得始终
             * residence : 北京市丰台区
             * fans : 4
             * follow : 9
             * favorite : 17
             * publish : 0
             * share : 0
             * cate_pid : 320
             * avatar : https://toutiao.28.com/data/upload/personal_avatars//180116/5a5d6d4f1c40f.jpeg_thumb.jpeg
             * utype : 2
             * username : t2297sgdr
             * is_enterprise_auth_user : 0
             * company : {"id":"1","uid":"1","enterprise_name":"商机头条","logo":"https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png","sign":"创业保险 签约付费","company_name":"","cate_id":"337"}
             */
            public String wx_page_type;
            public String uid;
            public String nickname;
            public String realname;
            public String phone;
            public String age;
            public String sex_cn;
            public String sex;
            public String signature;
            public String residence;
            public String fans;
            public String follow;
            public String favorite;
            public String publish;
            public String share;
            public String cate_pid;
            public String avatar;
            public String utype;
            public String username;
            public String is_enterprise_auth_user;
            public CompanyBean company;
            public String inspect_count;
            public String is_default_password;
            public String weixin;
            public String email;
            public String telephone;
            public String job_cate_pid;
            public static class CompanyBean {
                /**
                 * id : 1
                 * uid : 1
                 * enterprise_name : 商机头条
                 * logo : https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png
                 * sign : 创业保险 签约付费
                 * company_name :
                 * cate_id : 337
                 */

                public String id;
                public String uid;
                public String enterprise_name;
                public String logo;
                public String sign;
                public String company_name;
                public String cate_id;
            }
        }

        public static class YunxinBean {
            /**
             * token : 39728f29edc3d9d87c683c5c23f2e5e1
             * account : 6
             */
            public String token;
            public String account;
        }
    }
}
