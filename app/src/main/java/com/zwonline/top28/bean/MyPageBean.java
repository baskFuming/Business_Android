package com.zwonline.top28.bean;

import java.util.List;

/**
 * 我的页面埋点布局的bean
 */
public class MyPageBean {

    /**
     * status : 1
     * msg : 成功
     * data : [{"section":"快速发布","image":"mine-section-qucikpost","functions":[[{"title":"发布文章","image":"wode_fbwz","link":"ArticleActivity","eventId":"click_publish_article"},{"title":"转载文章","image":"wode_zzwz","link":"TransmitActivity","eventId":"click_transfer_article"}]]},{"section":"常用功能","image":"mine-section-commonfunc","functions":[[{"title":"钱包","image":"wode_qb","link":"WalletActivity","eventId":"click_wallet"},{"title":"鞅分挖矿","image":"wode_jf","link":"HashrateActivity","eventId":"click_point"},{"title":"守约宝","image":"wode_syb","link":"https://toutiao.28.com/Members/shouyuebao.html","eventId":"click_shouyuebao"},{"title":"推荐用户","image":"wode_tjyh","link":"RecommendUserActivity","eventId":"click_recommend_user"}]]},{"section":"我的操作","image":"mine-section-myactions","functions":[[{"title":"我的发布","image":"wode_wdfb","link":"MyIssueActivity","eventId":"click_personal_publish"},{"title":"我的分享","image":"wode_wdfx","link":"MyShareActivity","eventId":"click_personal_share"},{"title":"我的考察","image":"wode_wdkc","link":"MyExamineActivity","eventId":"click_personal_investigation"}]]},{"section":"企业服务","image":"https://toutiao.28.com/Application/Home/View/public/img/index-icon15.png","functions":[[{"title":"认证企业","image":"wode_rzqy","link":"AeosActivity","eventId":"click_enterprise_auth"}]]}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * section : 快速发布
         * image : mine-section-qucikpost
         * functions : [[{"title":"发布文章","image":"wode_fbwz","link":"ArticleActivity","eventId":"click_publish_article"},{"title":"转载文章","image":"wode_zzwz","link":"TransmitActivity","eventId":"click_transfer_article"}]]
         */

        public String section;
        public String image;
        public List<List<FunctionsBean>> functions;

        public static class FunctionsBean {
            /**
             * title : 发布文章
             * image : wode_fbwz
             * link : ArticleActivity
             * eventId : click_publish_article
             */

            public String title;
            public String image;
            public String link;
            public String eventId;
            public String titleBarColor;
        }
    }
}
