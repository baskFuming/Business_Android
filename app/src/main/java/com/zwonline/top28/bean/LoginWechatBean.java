package com.zwonline.top28.bean;

public class LoginWechatBean {

    /**
     * status : 1
     * msg : 登录成功
     * data : {"url":"/App/Members/index.html","yunxin":{"token":"73df262d2c22a39859f470a3df72a20f","account":"115679"},"user":{"uid":"115679","nickname":"9373retw","realname":"","phone":"18845339373","email":"","telephone":"","weixin":"","age":"0","sex":"1","sex_cn":"男","signature":"","residence":"","fans":"0","follow":"0","favorite":"0","publish":"0","share":"0","cate_pid":"0","job_cate_pid":"0","enterprise":"","position":"","avatar":"/data/upload/resource/no_photo_male.png","utype":"2","username":"9373retw","is_enterprise_auth_user":"0","from_site":"0","identity_type":"0","enter_team_confirm":"0","is_default_password":"1","inspect_count":"0","wx_page_type":"1"}}
     * dialog :
     */

    private int status;
    private String msg;
    private DataBean data;
    private String dialog;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public static class DataBean {
        /**
         * url : /App/Members/index.html
         * yunxin : {"token":"73df262d2c22a39859f470a3df72a20f","account":"115679"}
         * user : {"uid":"115679","nickname":"9373retw","realname":"","phone":"18845339373","email":"","telephone":"","weixin":"","age":"0","sex":"1","sex_cn":"男","signature":"","residence":"","fans":"0","follow":"0","favorite":"0","publish":"0","share":"0","cate_pid":"0","job_cate_pid":"0","enterprise":"","position":"","avatar":"/data/upload/resource/no_photo_male.png","utype":"2","username":"9373retw","is_enterprise_auth_user":"0","from_site":"0","identity_type":"0","enter_team_confirm":"0","is_default_password":"1","inspect_count":"0","wx_page_type":"1"}
         */

        private String url;
        private YunxinBean yunxin;
        private UserBean user;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public YunxinBean getYunxin() {
            return yunxin;
        }

        public void setYunxin(YunxinBean yunxin) {
            this.yunxin = yunxin;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class YunxinBean {
            /**
             * token : 73df262d2c22a39859f470a3df72a20f
             * account : 115679
             */

            private String token;
            private String account;

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }
        }

        public static class UserBean {
            /**
             * uid : 115679
             * nickname : 9373retw
             * realname :
             * phone : 18845339373
             * email :
             * telephone :
             * weixin :
             * age : 0
             * sex : 1
             * sex_cn : 男
             * signature :
             * residence :
             * fans : 0
             * follow : 0
             * favorite : 0
             * publish : 0
             * share : 0
             * cate_pid : 0
             * job_cate_pid : 0
             * enterprise :
             * position :
             * avatar : /data/upload/resource/no_photo_male.png
             * utype : 2
             * username : 9373retw
             * is_enterprise_auth_user : 0
             * from_site : 0
             * identity_type : 0
             * enter_team_confirm : 0
             * is_default_password : 1
             * inspect_count : 0
             * wx_page_type : 1
             */

            private String uid;
            private String nickname;
            private String realname;
            private String phone;
            private String email;
            private String telephone;
            private String weixin;
            private String age;
            private String sex;
            private String sex_cn;
            private String signature;
            private String residence;
            private String fans;
            private String follow;
            private String favorite;
            private String publish;
            private String share;
            private String cate_pid;
            private String job_cate_pid;
            private String enterprise;
            private String position;
            private String avatar;
            private String utype;
            private String username;
            private String is_enterprise_auth_user;
            private String from_site;
            private String identity_type;
            private String enter_team_confirm;
            private String is_default_password;
            private String inspect_count;
            private String wx_page_type;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getRealname() {
                return realname;
            }

            public void setRealname(String realname) {
                this.realname = realname;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getWeixin() {
                return weixin;
            }

            public void setWeixin(String weixin) {
                this.weixin = weixin;
            }

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getSex_cn() {
                return sex_cn;
            }

            public void setSex_cn(String sex_cn) {
                this.sex_cn = sex_cn;
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }

            public String getResidence() {
                return residence;
            }

            public void setResidence(String residence) {
                this.residence = residence;
            }

            public String getFans() {
                return fans;
            }

            public void setFans(String fans) {
                this.fans = fans;
            }

            public String getFollow() {
                return follow;
            }

            public void setFollow(String follow) {
                this.follow = follow;
            }

            public String getFavorite() {
                return favorite;
            }

            public void setFavorite(String favorite) {
                this.favorite = favorite;
            }

            public String getPublish() {
                return publish;
            }

            public void setPublish(String publish) {
                this.publish = publish;
            }

            public String getShare() {
                return share;
            }

            public void setShare(String share) {
                this.share = share;
            }

            public String getCate_pid() {
                return cate_pid;
            }

            public void setCate_pid(String cate_pid) {
                this.cate_pid = cate_pid;
            }

            public String getJob_cate_pid() {
                return job_cate_pid;
            }

            public void setJob_cate_pid(String job_cate_pid) {
                this.job_cate_pid = job_cate_pid;
            }

            public String getEnterprise() {
                return enterprise;
            }

            public void setEnterprise(String enterprise) {
                this.enterprise = enterprise;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getUtype() {
                return utype;
            }

            public void setUtype(String utype) {
                this.utype = utype;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getIs_enterprise_auth_user() {
                return is_enterprise_auth_user;
            }

            public void setIs_enterprise_auth_user(String is_enterprise_auth_user) {
                this.is_enterprise_auth_user = is_enterprise_auth_user;
            }

            public String getFrom_site() {
                return from_site;
            }

            public void setFrom_site(String from_site) {
                this.from_site = from_site;
            }

            public String getIdentity_type() {
                return identity_type;
            }

            public void setIdentity_type(String identity_type) {
                this.identity_type = identity_type;
            }

            public String getEnter_team_confirm() {
                return enter_team_confirm;
            }

            public void setEnter_team_confirm(String enter_team_confirm) {
                this.enter_team_confirm = enter_team_confirm;
            }

            public String getIs_default_password() {
                return is_default_password;
            }

            public void setIs_default_password(String is_default_password) {
                this.is_default_password = is_default_password;
            }

            public String getInspect_count() {
                return inspect_count;
            }

            public void setInspect_count(String inspect_count) {
                this.inspect_count = inspect_count;
            }

            public String getWx_page_type() {
                return wx_page_type;
            }

            public void setWx_page_type(String wx_page_type) {
                this.wx_page_type = wx_page_type;
            }
        }
    }
}
