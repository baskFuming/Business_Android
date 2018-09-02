package com.zwonline.top28.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 红包记录
 */
public class HongBaoLogBean implements Serializable {

    /**
     * status : 1
     * msg : 成功
     * data : [{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180630/5b371efdde0be.png_100x100.jpg","nickname":"t0061","amount":"0.98","add_time":"2018-07-03 13:53:03"},{"avatars":"https://toutiao.28.com/data/upload/personal_avatars//180625/5b308a42c5302.png_100x100.jpg","nickname":"雲焘","amount":"0.02","add_time":"2018-07-03 13:52:58"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * avatars : https://toutiao.28.com/data/upload/personal_avatars//180630/5b371efdde0be.png_100x100.jpg
         * nickname : t0061
         * amount : 0.98
         * add_time : 2018-07-03 13:53:03
         */

        public String avatars;
        public String nickname;
        public String amount;
        public String add_time;

    }
}
