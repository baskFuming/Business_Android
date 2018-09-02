package com.zwonline.top28.bean;

import java.util.List;

public class AddFriendBean {

    /**
     * status : 1
     * msg : 成功
     * data : [{"account":"394","nickname":"asdasdasd","avatars":"","mobile":"17343010819"},{"account":"193","nickname":"asdasdas","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180518/5afea0398bb79.png_100x100.jpg","mobile":"15020368972"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * account : 394
         * nickname : asdasdasd
         * avatars :
         * mobile : 17343010819
         */

        public String account;
        public String nickname;
        public String avatars;
        public String mobile;
        public String signature;
    }
}
