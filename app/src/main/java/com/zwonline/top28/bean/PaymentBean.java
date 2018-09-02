package com.zwonline.top28.bean;


import com.zwonline.top28.base.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
     * 描述：收付款记录
     * @author YSG
     * @date 2017/12/25
     */
public class PaymentBean extends BaseBean implements Serializable{

    /**
     * status : 1
     * msg :
     * data : [{"id":"222","oid":"W-171226-235627-26ca95","create_uid":"97","payment_uid":"0","utype":"2","is_paid":"1","amount":"1.00","user_amount":"1.00","payment_fee":"0.00","pay_amount":"0.00","pay_type":"2","payment":"wxpay","payment_cn":"微信支付","title":"付款给:t0060czib 1元","desc":"付款给:t0060czib 1元","cdate":"2017-12-26","ctime":"2017-12-26 23:56:27","payment_time":"0000-00-00 00:00:00","isdel":"0","order_type":"1","is_confirm":"0","create_member":{"uid":"97","nickname":"t0060czib","realname":"","phone":"18513690060","age":"0","sex_cn":"男","signature":"","residence":"","fans":"1","follow":"2","favorite":"5","publish":"0","share":"0","cate_pid":"0","avatar":"/data/upload/resource/no_photo_female.png","username":"t0060czib","is_enterprise_auth_user":"0"}},{"id":"221","oid":"W-171226-235528-a19b260","create_uid":"97","payment_uid":"0","utype":"2","is_paid":"1","amount":"1.00","user_amount":"1.00","payment_fee":"0.00","pay_amount":"0.00","pay_type":"2","payment":"wxpay","payment_cn":"微信支付","title":"付款给:t0060czib 1元","desc":"付款给:t0060czib 1元","cdate":"2017-12-26","ctime":"2017-12-26 23:55:28","payment_time":"0000-00-00 00:00:00","isdel":"0","order_type":"1","is_confirm":"0","create_member":{"uid":"97","nickname":"t0060czib","realname":"","phone":"18513690060","age":"0","sex_cn":"男","signature":"","residence":"","fans":"1","follow":"2","favorite":"5","publish":"0","share":"0","cate_pid":"0","avatar":"/data/upload/resource/no_photo_female.png","username":"t0060czib","is_enterprise_auth_user":"0"}},{"id":"220","oid":"W-171226-235511-3c29867","create_uid":"97","payment_uid":"0","utype":"2","is_paid":"1","amount":"10.00","user_amount":"10.00","payment_fee":"0.00","pay_amount":"0.00","pay_type":"2","payment":"wxpay","payment_cn":"微信支付","title":"付款给:t0060czib 10元","desc":"付款给:t0060czib 10元","cdate":"2017-12-26","ctime":"2017-12-26 23:55:11","payment_time":"0000-00-00 00:00:00","isdel":"0","order_type":"1","is_confirm":"0","create_member":{"uid":"97","nickname":"t0060czib","realname":"","phone":"18513690060","age":"0","sex_cn":"男","signature":"","residence":"","fans":"1","follow":"2","favorite":"5","publish":"0","share":"0","cate_pid":"0","avatar":"/data/upload/resource/no_photo_female.png","username":"t0060czib","is_enterprise_auth_user":"0"}},{"id":"109","oid":"W-171206-100440-a44a849","create_uid":"97","payment_uid":"0","utype":"2","is_paid":"1","amount":"10000.00","user_amount":"10000.00","payment_fee":"0.00","pay_amount":"0.00","pay_type":"2","payment":"wxpay","payment_cn":"微信支付","title":"付款给:t0060czib 10000元","desc":"付款给:t0060czib 10000元","cdate":"2017-12-06","ctime":"2017-12-06 10:04:40","payment_time":"0000-00-00 00:00:00","isdel":"0","order_type":"1","is_confirm":"0","create_member":{"uid":"97","nickname":"t0060czib","realname":"","phone":"18513690060","age":"0","sex_cn":"男","signature":"","residence":"","fans":"1","follow":"2","favorite":"5","publish":"0","share":"0","cate_pid":"0","avatar":"/data/upload/resource/no_photo_female.png","username":"t0060czib","is_enterprise_auth_user":"0"}}]
     * dialog :
     */

    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 222
         * oid : W-171226-235627-26ca95
         * create_uid : 97
         * payment_uid : 0
         * utype : 2
         * is_paid : 1
         * amount : 1.00
         * user_amount : 1.00
         * payment_fee : 0.00
         * pay_amount : 0.00
         * pay_type : 2
         * payment : wxpay
         * payment_cn : 微信支付
         * title : 付款给:t0060czib 1元
         * desc : 付款给:t0060czib 1元
         * cdate : 2017-12-26
         * ctime : 2017-12-26 23:56:27
         * payment_time : 0000-00-00 00:00:00
         * isdel : 0
         * order_type : 1
         * is_confirm : 0
         * create_member : {"uid":"97","nickname":"t0060czib","realname":"","phone":"18513690060","age":"0","sex_cn":"男","signature":"","residence":"","fans":"1","follow":"2","favorite":"5","publish":"0","share":"0","cate_pid":"0","avatar":"/data/upload/resource/no_photo_female.png","username":"t0060czib","is_enterprise_auth_user":"0"}
         */

        public String id;
        public String oid;
        public String create_uid;
        public String payment_uid;
        public String utype;
        public String is_paid;
        public String amount;
        public String user_amount;
        public String payment_fee;
        public String pay_amount;
        public String pay_type;
        public String payment;
        public String payment_cn;
        public String title;
        public String desc;
        public String cdate;
        public String ctime;
        public String payment_time;
        public String isdel;
        public String order_type;
        public String is_confirm;
        public CreateMemberBean create_member;

        public static class CreateMemberBean {
            /**
             * uid : 97
             * nickname : t0060czib
             * realname :
             * phone : 18513690060
             * age : 0
             * sex_cn : 男
             * signature :
             * residence :
             * fans : 1
             * follow : 2
             * favorite : 5
             * publish : 0
             * share : 0
             * cate_pid : 0
             * avatar : /data/upload/resource/no_photo_female.png
             * username : t0060czib
             * is_enterprise_auth_user : 0
             */

            public String uid;
            public String nickname;
            public String realname;
            public String phone;
            public String age;
            public String sex_cn;
            public String signature;
            public String residence;
            public String fans;
            public String follow;
            public String favorite;
            public String publish;
            public String share;
            public String cate_pid;
            public String avatar;
            public String username;
            public String is_enterprise_auth_user;
        }
    }
}
