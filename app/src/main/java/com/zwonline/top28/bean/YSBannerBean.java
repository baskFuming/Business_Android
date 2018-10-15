package com.zwonline.top28.bean;

import java.util.List;

public class YSBannerBean {

    /**
     * status : 1
     * msg : 成功
     * data : [{"name":"商机号个人会员","img":"https://store-toutiao.28.com/public/attachment/201810/09/14/20181009145344.png","jump_url":"https://store-toutiao.28.com/wap/index.php?ctl=deal&act=show&id=14"},{"name":"商机头条合伙人","img":"https://store-toutiao.28.com/public/attachment/201809/13/16/20180913163729.png","jump_url":"https://store-toutiao.28.com/wap/index.php?ctl=deal&act=show&id=13"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * name : 商机号个人会员
         * img : https://store-toutiao.28.com/public/attachment/201810/09/14/20181009145344.png
         * jump_url : https://store-toutiao.28.com/wap/index.php?ctl=deal&act=show&id=14
         */

        public String name;
        public String img;
        public String jump_url;
    }
}
