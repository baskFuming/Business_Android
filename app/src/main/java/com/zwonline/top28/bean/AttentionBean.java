package com.zwonline.top28.bean;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class AttentionBean {

    /**
     * data : {}
     * dialog :
     * msg : 关注成功
     * status : 1
     */

    public DataBean data;
    public String dialog;
    public String msg;
    public int status;

    public static class DataBean {
        public String newest_like_count;
        public String tag_id;
        public String unread_count;
        public int sortNum;
        public String computePower;//充值商机币赠送算力数量

    }
}
