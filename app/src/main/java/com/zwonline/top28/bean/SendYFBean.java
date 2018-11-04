package com.zwonline.top28.bean;

import android.test.suitebuilder.annotation.Suppress;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 发鞅分bean
 */
public class SendYFBean implements Serializable {

    /**
     * status : 1
     * msg : 成功
     * data : {"hongbao_id":1,"user_id":227,"postscript":"大吉大利，今晚吃鸡","total_amount":1,"total_package":10,"random_flag":1,"expire_time":1529601630}
     */

    public int status;
    public String msg;
    public DataBean data;

    public static class DataBean {
        /**
         * hongbao_id : 1
         * user_id : 227
         * postscript : 大吉大利，今晚吃鸡
         * total_amount : 1
         * total_package : 10
         * random_flag : 1
         * expire_time : 1529601630
         */

        public int hongbao_id;
        public int user_id;
        public String postscript;
        public int total_amount;
        public int total_package;
        public int random_flag;
        public int expire_time;
    }
}
