package com.zwonline.top28.bean;

import java.util.List;

/**
     * 描述：提现记录
     * @author YSG
     * @date 2018/1/10
     */
public class WithdrawRecordBean {

    /**
     * status : 1
     * msg :
     * data : [{"id":"2","card_id":"2","card_number":"62012932793289748932","card_uid":"16","visitor_username":"t5326jwfe","card_holder":"卢壮","cash":"200","add_time":"2017-11-27 13:55:45","status":"1","admin_id":"1","admin_username":"xinghailong","review_time":"2017-11-27 14:22:51"},{"id":"3","card_id":"2","card_number":"62012932793289748932","card_uid":"16","visitor_username":"t5326jwfe","card_holder":"卢壮","cash":"1","add_time":"2017-12-05 10:01:11","status":"1","admin_id":"1","admin_username":"xinghailong","review_time":"2017-12-05 10:27:14"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 2
         * card_id : 2
         * card_number : 62012932793289748932
         * card_uid : 16
         * visitor_username : t5326jwfe
         * card_holder : 卢壮
         * cash : 200
         * add_time : 2017-11-27 13:55:45
         * status : 1
         * admin_id : 1
         * admin_username : xinghailong
         * review_time : 2017-11-27 14:22:51
         */

        public String id;
        public String card_id;
        public String card_number;
        public String card_uid;
        public String visitor_username;
        public String card_holder;
        public String cash;
        public String add_time;
        public String status;
        public String admin_id;
        public String admin_username;
        public String review_time;
    }
}
