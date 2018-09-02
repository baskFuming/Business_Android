package com.zwonline.top28.bean;

import java.util.List;

/**
 * @author YSG
 * @desc赚取积分说明
 * @date ${Date}
 */
public class EarnIntegralBean {

    /**
     * data : [{"complete_times":"0","completed":"1","once":"1","points":"10","times":"0","title":"注册账号"},{"complete_times":"0","completed":"0","once":"0","points":"100","times":"5","title":"邀请好友注册"},{"complete_times":"0","completed":"0","once":"0","points":"10","times":"5","title":"发布文章"},{"complete_times":"0","completed":"0","once":"1","points":"10","times":"5","title":"关注用户"},{"complete_times":"0","completed":"0","once":"1","points":"20","times":"5","title":"被用户关注"},{"complete_times":"0","completed":"0","once":"0","points":"10","times":"5","title":"发表评论"},{"complete_times":"0","completed":"0","once":"0","points":"10","times":"5","title":"分享文章"},{"complete_times":"0","completed":"0","once":"0","points":"1","times":"5","title":"收藏文章"},{"complete_times":"0","completed":"0","once":"0","points":"1","times":"5","title":"接听企业来电"},{"complete_times":"0","completed":"0","once":"1","points":"1","times":"5","title":"同意接听企业来电"}]
     * dialog :
     * msg : 成功
     * status : 1
     */

    public String dialog;
    public String msg;
    public int status;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * complete_times : 0
         * completed : 1
         * once : 1
         * points : 10
         * times : 0
         * title : 注册账号
         */

        public String complete_times;
        public String completed;
        public String once;
        public String points;
        public String times;
        public String title;
    }
}
