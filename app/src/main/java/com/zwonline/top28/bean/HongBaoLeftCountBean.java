package com.zwonline.top28.bean;

import java.io.Serializable;

/**
 * 红包余量查询
 */
public class HongBaoLeftCountBean implements Serializable{

    /**
     * data : {"alreadyGet":1,"getAmount":"0.74","hasGetAmount":"1.00","hasGetPackageCount":2,"totalAmount":"1","totalPackageCount":2}
     * msg :
     * status : 1
     */

    public DataBean data;
    public String msg;
    public int status;

    public static class DataBean {
        /**
         * alreadyGet : 1
         * getAmount : 0.74
         * hasGetAmount : 1.00
         * hasGetPackageCount : 2
         * totalAmount : 1
         * totalPackageCount : 2
         */

        public int alreadyGet;
        public String getAmount;
        public String hasGetAmount;
        public int hasGetPackageCount;
        public String totalAmount;
        public int totalPackageCount;
        public int  expireFlag;
    }
}
