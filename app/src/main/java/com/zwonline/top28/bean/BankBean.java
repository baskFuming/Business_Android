package com.zwonline.top28.bean;

import java.util.List;

/**
     * 描述：我的银行卡
     * @author YSG
     * @date 2017/12/27
     */
public class BankBean {

    /**
     * status : 1
     * msg :
     * data : [{"id":"5","company_id":"0","uid":"97","card_number":"6214830181095992","cardholder":"于帅光","add_time":"2017-12-27 11:03:06","card_bank":"招商银行"},{"id":"7","company_id":"0","uid":"97","card_number":"62170001320386872","cardholder":"xxx","add_time":"2017-12-27 11:47:34","card_bank":"建设银行"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 5
         * company_id : 0
         * uid : 97
         * card_number : 6214830181095992
         * cardholder : 于帅光
         * add_time : 2017-12-27 11:03:06
         * card_bank : 招商银行
         */

        public String id;
        public String company_id;
        public String uid;
        public String card_number;
        public String cardholder;
        public String add_time;
        public String card_bank;
    }
}
