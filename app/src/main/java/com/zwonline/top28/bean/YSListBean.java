package com.zwonline.top28.bean;

import java.util.List;

public class YSListBean {

    /**
     * data : {"list":[{"brief":"【商机号】个人会员--年度会员火热预售中\u2026\u2026不一样的价格享受相同的众多增值福利！预售时间有限，速速来抢！！截止时间为本月底。","image":"https://store-toutiao.28.com/public/attachment/201809/29/13/20180929135930.png","name":"【商机号】个人会员--年度会员","price":"278.00","url":"https://store-toutiao.28.com//wap/index.php?ctl=deal&act=show&id=16&from_type=1"},{"brief":"商机号个人会员--季度会员火热预售中\u2026\u2026不一样的价格享受相同的众多增值福利！预售时间有限，速速来抢！！截止时间为本月底。","image":"https://store-toutiao.28.com/public/attachment/201809/29/13/20180929135307.png","name":"【商机号】个人会员--季度会员","price":"78.00","url":"https://store-toutiao.28.com//wap/index.php?ctl=deal&act=show&id=15&from_type=1"},{"brief":"商机号个人会员--月度会员火热预售中\u2026\u2026不一样的价格享受同样的众多增值福利。预售时间有限，速速来抢！！截止日期为本月底！","image":"https://store-toutiao.28.com/public/attachment/201809/29/13/20180929135013.png","name":"【商机号】个人会员--月度会员","price":"29.00","url":"https://store-toutiao.28.com//wap/index.php?ctl=deal&act=show&id=14&from_type=1"},{"brief":"商机头条合伙人旨在让每个人都成为CEO，在自主经营体系下共同把事业做大，分享共同成就。请进入详情具体了解。","image":"https://store-toutiao.28.com/public/attachment/201809/13/14/20180913144918.png","name":"商机头条合伙人","price":"10000.00","url":"https://store-toutiao.28.com//wap/index.php?ctl=deal&act=show&id=13&from_type=1"}],"rs_count":"4"}
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
         * list : [{"brief":"【商机号】个人会员--年度会员火热预售中\u2026\u2026不一样的价格享受相同的众多增值福利！预售时间有限，速速来抢！！截止时间为本月底。","image":"https://store-toutiao.28.com/public/attachment/201809/29/13/20180929135930.png","name":"【商机号】个人会员--年度会员","price":"278.00","url":"https://store-toutiao.28.com//wap/index.php?ctl=deal&act=show&id=16&from_type=1"},{"brief":"商机号个人会员--季度会员火热预售中\u2026\u2026不一样的价格享受相同的众多增值福利！预售时间有限，速速来抢！！截止时间为本月底。","image":"https://store-toutiao.28.com/public/attachment/201809/29/13/20180929135307.png","name":"【商机号】个人会员--季度会员","price":"78.00","url":"https://store-toutiao.28.com//wap/index.php?ctl=deal&act=show&id=15&from_type=1"},{"brief":"商机号个人会员--月度会员火热预售中\u2026\u2026不一样的价格享受同样的众多增值福利。预售时间有限，速速来抢！！截止日期为本月底！","image":"https://store-toutiao.28.com/public/attachment/201809/29/13/20180929135013.png","name":"【商机号】个人会员--月度会员","price":"29.00","url":"https://store-toutiao.28.com//wap/index.php?ctl=deal&act=show&id=14&from_type=1"},{"brief":"商机头条合伙人旨在让每个人都成为CEO，在自主经营体系下共同把事业做大，分享共同成就。请进入详情具体了解。","image":"https://store-toutiao.28.com/public/attachment/201809/13/14/20180913144918.png","name":"商机头条合伙人","price":"10000.00","url":"https://store-toutiao.28.com//wap/index.php?ctl=deal&act=show&id=13&from_type=1"}]
         * rs_count : 4
         */

        public String rs_count;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * brief : 【商机号】个人会员--年度会员火热预售中……不一样的价格享受相同的众多增值福利！预售时间有限，速速来抢！！截止时间为本月底。
             * image : https://store-toutiao.28.com/public/attachment/201809/29/13/20180929135930.png
             * name : 【商机号】个人会员--年度会员
             * price : 278.00
             * url : https://store-toutiao.28.com//wap/index.php?ctl=deal&act=show&id=16&from_type=1
             */

            public String brief;
            public String image;
            public String name;
            public String price;
            public String url;
        }
    }
}
