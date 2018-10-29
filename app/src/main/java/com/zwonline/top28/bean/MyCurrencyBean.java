package com.zwonline.top28.bean;


import java.util.List;

/**
 * 描述：我的创业币
 *
 * @author YSG
 * @date 2017/12/25
 */
public class MyCurrencyBean {

    /**
     * status : 1
     * msg :
     * data : {"point":"470","cost_day":"45","list":[{"id":"2326","uid":"16","htype":"task_comment_article","htype_cn":"发表文章评论","operate":"1","points":"10","addtime":"1513581786"},{"id":"2325","uid":"16","htype":"task_comment_article","htype_cn":"发表文章评论","operate":"1","points":"10","addtime":"1513581669"},{"id":"2324","uid":"16","htype":"task_comment_article","htype_cn":"发表文章评论","operate":"1","points":"10","addtime":"1513581559"},{"id":"2323","uid":"16","htype":"task_comment_article","htype_cn":"发表文章评论","operate":"1","points":"10","addtime":"1513581371"},{"id":"2293","uid":"16","htype":"task_comment_article","htype_cn":"发表文章评论","operate":"1","points":"10","addtime":"1513579303"},{"id":"2245","uid":"16","htype":"task_be_attention","htype_cn":"被关注","operate":"1","points":"20","addtime":"1513561596"},{"id":"1965","uid":"16","htype":"task_attention","htype_cn":"关注用户","operate":"1","points":"10","addtime":"1513222076"},{"id":"1215","uid":"16","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10","addtime":"1512527564"},{"id":"1213","uid":"16","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10","addtime":"1512527448"},{"id":"1208","uid":"16","htype":"task_invitation_reg","htype_cn":"邀请注册","operate":"1","points":"100","addtime":"1512527078"}]}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * point : 470
         * cost_day : 45
         * list : [{"id":"2326","uid":"16","htype":"task_comment_article","htype_cn":"发表文章评论","operate":"1","points":"10","addtime":"1513581786"},{"id":"2325","uid":"16","htype":"task_comment_article","htype_cn":"发表文章评论","operate":"1","points":"10","addtime":"1513581669"},{"id":"2324","uid":"16","htype":"task_comment_article","htype_cn":"发表文章评论","operate":"1","points":"10","addtime":"1513581559"},{"id":"2323","uid":"16","htype":"task_comment_article","htype_cn":"发表文章评论","operate":"1","points":"10","addtime":"1513581371"},{"id":"2293","uid":"16","htype":"task_comment_article","htype_cn":"发表文章评论","operate":"1","points":"10","addtime":"1513579303"},{"id":"2245","uid":"16","htype":"task_be_attention","htype_cn":"被关注","operate":"1","points":"20","addtime":"1513561596"},{"id":"1965","uid":"16","htype":"task_attention","htype_cn":"关注用户","operate":"1","points":"10","addtime":"1513222076"},{"id":"1215","uid":"16","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10","addtime":"1512527564"},{"id":"1213","uid":"16","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10","addtime":"1512527448"},{"id":"1208","uid":"16","htype":"task_invitation_reg","htype_cn":"邀请注册","operate":"1","points":"100","addtime":"1512527078"}]
         */

        public String point;
        public String cost_day;
        public String balance;
        public String freeze_amount;

        public List<ListBean> list;

        public static class ListBean {
            /**
             * id : 2326
             * uid : 16
             * htype : task_comment_article
             * htype_cn : 发表文章评论
             * operate : 1
             * points : 10
             * addtime : 1513581786
             */

            public String id;
            public String uid;
            public String htype;
            public String htype_cn;
            public String operate;
            public String points;
            public String addtime;
        }
    }
}
