package com.zwonline.top28.bean;

import java.util.List;


/**
 * 描述：我的账单
 * @author YSG
 * @date 2017/12/25
 */
public class MyBillBean {

/**
 * status : 1
 * msg :
 * data : [{"username":"t2296ikxb","avatars":"","id":"28","from_uid":"99","type":"1","title":"[t2296ikxb]支付分成","before_balance":"2.61","after_balance":"2.66","amount":"0.05","add_time":"2017-12-06 15:31:46"},{"username":"t2296ikxb","avatars":"","id":"24","from_uid":"99","type":"1","title":"[t2296ikxb]支付1.00元","before_balance":"2.56","after_balance":"2.61","amount":"0.05","add_time":"2017-12-06 11:13:20"},{"username":"t2296ikxb","avatars":"","id":"19","from_uid":"99","type":"1","title":"[t2296ikxb]支付1.00元","before_balance":"2.53","after_balance":"2.56","amount":"0.03","add_time":"2017-12-06 11:09:44"},{"username":"t2296ikxb","avatars":"","id":"14","from_uid":"99","type":"1","title":"[t2296ikxb]支付1.00元","before_balance":"2.50","after_balance":"2.53","amount":"0.03","add_time":"2017-12-06 11:04:59"},{"username":"t2296ikxb","avatars":"","id":"10","from_uid":"99","type":"1","title":"收款分成","before_balance":"2.40","after_balance":"2.50","amount":"0.10","add_time":"2017-12-06 10:57:42"},{"username":"t2296ikxb","avatars":"","id":"7","from_uid":"99","type":"1","title":"[t2296ikxb]支付1.00元","before_balance":"1.40","after_balance":"2.40","amount":"1.00","add_time":"2017-12-06 10:26:18"},{"username":[],"avatars":[],"id":"5","from_uid":"0","type":"0","title":"付款给[t2297sgdr]","before_balance":"2.40","after_balance":"1.40","amount":"1.00","add_time":"2017-12-06 10:07:10"},{"username":[],"avatars":[],"id":"3","from_uid":"0","type":"1","title":"微信充值","before_balance":"1.40","after_balance":"2.40","amount":"1.00","add_time":"2017-12-06 10:06:54"},{"username":[],"avatars":[],"id":"2","from_uid":"0","type":"0","title":"购买服务[超级微植入]","before_balance":"1.50","after_balance":"1.40","amount":"0.10","add_time":"2017-12-05 17:12:32"},{"username":[],"avatars":[],"id":"1","from_uid":"0","type":"1","title":"微信充值","before_balance":"1.40","after_balance":"1.50","amount":"0.10","add_time":"2017-12-05 17:12:32"}]
 * dialog :
 */

public int status;
public String msg;
public String dialog;
public List<DataBean> data;

public static class DataBean {
    /**
     * username : t2296ikxb
     * avatars :
     * id : 28
     * from_uid : 99
     * type : 1
     * title : [t2296ikxb]支付分成
     * before_balance : 2.61
     * after_balance : 2.66
     * amount : 0.05
     * add_time : 2017-12-06 15:31:46
     */

    public String username;
    public String avatars;
    public String id;
    public String from_uid;
    public String type;
    public String title;
    public String before_balance;
    public String after_balance;
    public String amount;
    public String add_time;
}
}
