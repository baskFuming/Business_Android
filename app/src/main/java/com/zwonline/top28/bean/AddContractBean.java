package com.zwonline.top28.bean;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class AddContractBean {

    /**
     * status : 1
     * msg : 添加成功
     * data : {"contract_id":"56"}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * contract_id : 56
         */

        public String contract_id;
    }
}
