package com.zwonline.top28.bean;

public class GetHongBaoBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"get_amount":"0.22"}
     */

    public int status;
    public String msg;
    public DataBean data;

    public static class DataBean {
        /**
         * get_amount : 0.22
         */

        public String get_amount;
    }
}
