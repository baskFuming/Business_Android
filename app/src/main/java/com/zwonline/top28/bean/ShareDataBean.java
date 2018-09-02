package com.zwonline.top28.bean;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class ShareDataBean {

    /**
     * status : 0
     * msg :
     * data : {"article_img":"https://toutiao.28.com/data/upload/article_img/1801/03/2cae435143020537bf76a862611ed7b6.jpg_150x150.jpg","article_desc":"大家好，今天和大家聊一下杜月笙，作为上海滩的无冕之王，杜月笙可以说是最成功的，但是他的成功和上海滩的其他人不同，因为杜月笙不仅会赚钱，还会花钱。他说看七种人，花七种钱，那么这句话是什么意思呢？第一种是..","article_url":"/Index/article/id/3080.html#showNavBar-pushNewView"}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * article_img : https://toutiao.28.com/data/upload/article_img/1801/03/2cae435143020537bf76a862611ed7b6.jpg_150x150.jpg
         * article_desc : 大家好，今天和大家聊一下杜月笙，作为上海滩的无冕之王，杜月笙可以说是最成功的，但是他的成功和上海滩的其他人不同，因为杜月笙不仅会赚钱，还会花钱。他说看七种人，花七种钱，那么这句话是什么意思呢？第一种是..
         * article_url : /Index/article/id/3080.html#showNavBar-pushNewView
         */
        public String article_img;
        public String article_desc;
        public String article_url;
    }
}
