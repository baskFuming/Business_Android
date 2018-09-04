package com.zwonline.top28.bean;

import java.util.List;

/**
 * 点赞列表
 */
public class LikeListBean {

    /**
     * status : 1
     * msg : 成功
     * data : [{"uid":"97","nickname":"雲焘","signature":"你好","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180713/5b482268646ef.png_100x100.jpg"},{"uid":"195","nickname":"全村的希望","signature":"全村空荡荡，希望在我肩","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180820/5b7a183fe22df.jpg_100x100.jpg"},{"uid":"224","nickname":"黑桃","signature":"事实上","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180816/5b754cac2feb6.jpg_100x100.jpg"},{"uid":"227","nickname":"MightyLu","signature":"talk is cheap show me your code","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180514/5af9255cbe3bb.jpg_100x100.jpg"},{"uid":"19278","nickname":"尼亚7","signature":"Wwwaaa","avatars":"https://toutiao.28.com/data/upload/personal_avatars//180816/5b74efbc92ec2.jpg_100x100.jpg"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * uid : 97
         * nickname : 雲焘
         * signature : 你好
         * avatars : https://toutiao.28.com/data/upload/personal_avatars//180713/5b482268646ef.png_100x100.jpg
         */

        public String uid;
        public String nickname;
        public String signature;
        public String avatars;
    }
}
