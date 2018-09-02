package com.zwonline.top28.bean;

public class BalancePayBean {

    /**
     * data : {"balance":"-8.60"}
     * dialog :
     * msg :
     * status : 充值成功
     */

    public DataBean data;
    public String dialog;
    public String msg;
    public String status;

    public static class DataBean {
        /**
         * balance : -8.60
         */

        public String balance;
    }
}
