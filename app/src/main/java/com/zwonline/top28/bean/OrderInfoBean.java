package com.zwonline.top28.bean;

/**
 * @author YSG
 * @desc订单详情bean
 * @date ${Date}
 */
public class OrderInfoBean {

    /**
     * data : {"amount":"0.10","buyer_id":"","buyer_logon_id":"","cdate":"2018-03-13","create_uid":"16","ctime":"2018-03-13 11:05:19","desc":"付款给:商机在线 [saonian] 0.1元","id":"57","is_confirm":"0","is_paid":"1","is_recharge_order":"0","isdel":"0","oid":"2018031310210149","order_type":"1","pay_amount":"0.00","pay_type":"0","payment":"","payment_cn":"","payment_fee":"0.00","payment_time":"0000-00-00 00:00:00","payment_uid":"0","project_id":"0","seller_email":"","seller_id":"","title":"付款给:商机在线 0.1元","user_amount":"0.10","utype":"0"}
     * dialog :
     * msg : 成功
     * status : 1
     */

    public DataBean data;
    public String dialog;
    public String msg;
    public int status;

    public static class DataBean {
        /**
         * amount : 0.10
         * buyer_id :
         * buyer_logon_id :
         * cdate : 2018-03-13
         * create_uid : 16
         * ctime : 2018-03-13 11:05:19
         * desc : 付款给:商机在线 [saonian] 0.1元
         * id : 57
         * is_confirm : 0
         * is_paid : 1
         * is_recharge_order : 0
         * isdel : 0
         * oid : 2018031310210149
         * order_type : 1
         * pay_amount : 0.00
         * pay_type : 0
         * payment :
         * payment_cn :
         * payment_fee : 0.00
         * payment_time : 0000-00-00 00:00:00
         * payment_uid : 0
         * project_id : 0
         * seller_email :
         * seller_id :
         * title : 付款给:商机在线 0.1元
         * user_amount : 0.10
         * utype : 0
         */

        public String amount;
        public String buyer_id;
        public String buyer_logon_id;
        public String cdate;
        public String create_uid;
        public String ctime;
        public String desc;
        public String id;
        public String is_confirm;
        public String is_paid;
        public String is_recharge_order;
        public String isdel;
        public String oid;
        public String order_type;
        public String pay_amount;
        public String pay_type;
        public String payment;
        public String payment_cn;
        public String payment_fee;
        public String payment_time;
        public String payment_uid;
        public String project_id;
        public String seller_email;
        public String seller_id;
        public String title;
        public String user_amount;
        public String utype;
        public String project_name;
    }
}
