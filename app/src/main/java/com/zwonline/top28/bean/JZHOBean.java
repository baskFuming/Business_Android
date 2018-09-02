package com.zwonline.top28.bean;

import java.util.List;

/**
 * @author YSG
 * @desc创业资讯
 * @date ${Date}
 */
public class JZHOBean {

    /**
     * status : 1
     * msg :
     * data : [{"title":"2017年中国食品饮料最新消费趋势报告","article_id":"128"},{"title":"一个人的格局有多大，看这三点","article_id":"171"},{"title":"你的死工资，正在拖垮你","article_id":"209"},{"title":"创业开店的财富秘籍是什么？","article_id":"298"},{"title":"西式快餐加盟 源动力汤姆之家选址灵活创业轻松","article_id":"401"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * title : 2017年中国食品饮料最新消费趋势报告
         * article_id : 128
         */

        public String title;
        public String article_id;
    }
}
