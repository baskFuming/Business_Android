package com.zwonline.top28.bean;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/8 16:01
 */

public class LoginBean {


    /**
     * status : 1
     * msg : 登录成功！
     * data : {"url":"/App/Members/index.html","yunxin":{"token":"f0207f20931e04bd11ee6c7b6dc763aa","account":"97"},"user":{"uid":"97","nickname":"t0060czib","sex_cn":"男","signature":"","residence":"","fans":"0","follow":"0","favorite":"1","publish":"0","share":"0","cate_pid":"0","avatar":"/data/upload/resource_photo_female.png","username":"t0060czib","is_enterprise_auth_user":"0"}}
     * dialog : ved0j4h2pafp5pfrqkl8gnjqi1
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
         * yunxin : {"token":"f0207f20931e04bd11ee6c7b6dc763aa","account":"97"}
         * user : {"uid":"97","nickname":"t0060czib","sex_cn":"男","signature":"","residence":"","fans":"0","follow":"0","favorite":"1","publish":"0","share":"0","cate_pid":"0","avatar":"/data/upload/resource_photo_female.png","username":"t0060czib","is_enterprise_auth_user":"0"}
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
             * token : f0207f20931e04bd11ee6c7b6dc763aa
             * account : 97
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
             * uid : 97
             * nickname : t0060czib
             * sex_cn : 男
             * signature :
             * residence :
             * fans : 0
             * follow : 0
             * favorite : 1
             * publish : 0
             * share: 0
             * cate_pid : 0
             * avatar : /data/upload/resource_photo_female.png
             * username : t0060czib
             * is_enterprise_auth_user : 0
             */

            private String uid;
            private String nickname;
            private String sex_cn;
            private String signature;
            private String residence;
            private String fans;
            private String follow;
            private String favorite;
            private String publish;
            private String share;
            private String cate_pid;
            private String avatar;
            private String username;
            private String is_enterprise_auth_user;

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

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
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
        }
    }
}
