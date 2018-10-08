package com.zwonline.top28.bean;

import java.util.List;

/**
 * Created by sdh on 2018/3/10.
 */

public class IntegralBean {

    /**
     * status : 1
     * msg :
     * data : {"point":"356.00000000","cost_day":"126","list":[{"id":"9120","uid":"115","htype":"task_attention","htype_cn":"关注用户","operate":"1","points":"10.00000000","addtime":"2018-03-08 09:25:15"},{"id":"9114","uid":"115","htype":"task_attention","htype_cn":"关注用户","operate":"1","points":"10.00000000","addtime":"2018-03-07 17:40:27"},{"id":"9112","uid":"115","htype":"task_attention","htype_cn":"关注用户","operate":"1","points":"10.00000000","addtime":"2018-03-07 17:40:24"},{"id":"9069","uid":"115","htype":"task_attention","htype_cn":"关注用户","operate":"1","points":"10.00000000","addtime":"2018-03-07 10:07:53"},{"id":"9054","uid":"115","htype":"task_attention","htype_cn":"关注用户","operate":"1","points":"10.00000000","addtime":"2018-03-06 15:00:03"},{"id":"7822","uid":"115","htype":"chat_with_fans","htype_cn":"与粉丝聊天","operate":"2","points":"2.00000000","addtime":"2018-02-05 17:08:18"},{"id":"7820","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:58:15"},{"id":"7819","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:55:23"},{"id":"7818","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:51:40"},{"id":"7816","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:40:41"},{"id":"7814","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:36:48"},{"id":"7809","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:30:46"},{"id":"7805","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:27:51"},{"id":"7799","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:24:12"},{"id":"7794","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 15:50:22"}]}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * point : 356.00000000
         * cost_day : 126
         * list : [{"id":"9120","uid":"115","htype":"task_attention","htype_cn":"关注用户","operate":"1","points":"10.00000000","addtime":"2018-03-08 09:25:15"},{"id":"9114","uid":"115","htype":"task_attention","htype_cn":"关注用户","operate":"1","points":"10.00000000","addtime":"2018-03-07 17:40:27"},{"id":"9112","uid":"115","htype":"task_attention","htype_cn":"关注用户","operate":"1","points":"10.00000000","addtime":"2018-03-07 17:40:24"},{"id":"9069","uid":"115","htype":"task_attention","htype_cn":"关注用户","operate":"1","points":"10.00000000","addtime":"2018-03-07 10:07:53"},{"id":"9054","uid":"115","htype":"task_attention","htype_cn":"关注用户","operate":"1","points":"10.00000000","addtime":"2018-03-06 15:00:03"},{"id":"7822","uid":"115","htype":"chat_with_fans","htype_cn":"与粉丝聊天","operate":"2","points":"2.00000000","addtime":"2018-02-05 17:08:18"},{"id":"7820","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:58:15"},{"id":"7819","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:55:23"},{"id":"7818","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:51:40"},{"id":"7816","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:40:41"},{"id":"7814","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:36:48"},{"id":"7809","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:30:46"},{"id":"7805","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:27:51"},{"id":"7799","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 16:24:12"},{"id":"7794","uid":"115","htype":"task_publish_article","htype_cn":"发布文章","operate":"1","points":"10.00000000","addtime":"2018-02-05 15:50:22"}]
         */

        public String point;
        public String cost_day;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * id : 9120
             * uid : 115
             * htype : task_attention
             * htype_cn : 关注用户
             * operate : 1
             * points : 10.00000000
             * addtime : 2018-03-08 09:25:15
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
