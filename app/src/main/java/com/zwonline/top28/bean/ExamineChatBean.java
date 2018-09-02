package com.zwonline.top28.bean;

/**
 * @author YSG
 * @desc检查是否和某人聊过天接口
 * @date ${Date}
 */
public class ExamineChatBean {

    /**
     * status : 1
     * msg :
     * data : {"chatted":"0","cost_point":"2"}
     * dialog : 7e124244bc7741d01f2beb5e1f38587a
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * chatted : 0
         * cost_point : 2
         */

        public String chatted;
        public String cost_point;
    }
}
