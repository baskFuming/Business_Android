package com.zwonline.top28.bean;

/**
 * 启动屏广告接口
 */
public class LanchScreenBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"img_url":"https://toutiao.28.com/data/static/img/test_launch_screen.jpeg","jump_url":"https://toutiao.28.com/Integral/redPacket","jump_out":"0"}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;
    public static class DataBean {
        /**
         * img_url : https://toutiao.28.com/data/static/img/test_launch_screen.jpeg
         * jump_url : https://toutiao.28.com/Integral/redPacket
         * jump_out : 0
         */
        public String img_url;
        public String jump_url;
        public int jump_out;
        public int count_down;
    }
}
