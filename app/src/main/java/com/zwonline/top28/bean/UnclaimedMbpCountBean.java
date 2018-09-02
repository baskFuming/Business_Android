package com.zwonline.top28.bean;

/**
 * 未领鞅分数量
 */
public class UnclaimedMbpCountBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"unclaimed_mbp_count":"0"}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * unclaimed_mbp_count : 0
         */

        public String unclaimed_mbp_count;
    }
}
