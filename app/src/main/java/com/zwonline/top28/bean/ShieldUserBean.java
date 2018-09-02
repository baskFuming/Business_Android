package com.zwonline.top28.bean;

import java.util.List;

/**
 * 屏蔽列表bean
 */
public class ShieldUserBean {

    /**
     * status : 1
     * msg :
     * data : [{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180630/5b3733331c95a.png_100x100.jpg","nickname":"外科医生wf","uid":"3177"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180711/5b4598e135944.jpg_100x100.jpg","nickname":"极势","uid":"3077"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * avatars : https://toutiao.28.com/data/upload/personal_avatars//180630/5b3733331c95a.png_100x100.jpg
         * nickname : 外科医生wf
         * uid : 3177
         */

        public String avatars;
        public String nickname;
        public String uid;
    }
}
