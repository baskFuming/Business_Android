package com.zwonline.top28.bean;

import java.util.List;

/**
 * @author YSG
 * @desc商机列表
 * @date 2017/12/14
 */
    

public class BusinessListBean {

    /**
     * status : 1
     * msg : success
     * data : [{"id":"47","uid":"71","cate_id":"336","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bc45dc28fc.png_thumb.png","title":"简致集成墙扣板","sign":"装修与科技碰撞 擦出家装新时代耀眼火花","cate_name":"家居建材"},{"id":"44","uid":"69","cate_id":"336","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bc060ba17e.png_thumb.png","title":"森族植物纤维泥","sign":"甲醛望而却步，健康向您招手","cate_name":"家居建材"},{"id":"26","uid":"51","cate_id":"336","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1ba785a698f.png_thumb.png","title":"涂奈克墙面大师","sign":"源自德国的墙面定制大师","cate_name":"家居建材"},{"id":"23","uid":"48","cate_id":"336","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1ba4e3b4dd3.png_thumb.png","title":"欧亿优品","sign":"全屋生态整装","cate_name":"家居建材"},{"id":"17","uid":"42","cate_id":"336","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1b8a753f39b.png_thumb.png","title":"赛维洗衣","sign":"畅想轻松健康轻生活","cate_name":"家居建材"},{"id":"15","uid":"40","cate_id":"336","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1b87fa7dddc.png_thumb.png","title":"星宫照明","sign":"智能照明专家 照亮前方财路","cate_name":"家居建材"},{"id":"12","uid":"37","cate_id":"336","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1b83101cac3.png_thumb.png_thumb.png","title":"美屋定制","sign":"智能 环保 养生 创意空间定制","cate_name":"家居建材"},{"id":"10","uid":"32","cate_id":"336","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1b84539fd75.png_thumb.png","title":"洁当家","sign":"一站式清洁服务专家","cate_name":"家居建材"}]
     * dialog : 37tjcbr9775otnikh0m3q0bl37
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 47
         * uid : 71
         * cate_id : 336
         * logo : https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bc45dc28fc.png_thumb.png
         * title : 简致集成墙扣板
         * sign : 装修与科技碰撞 擦出家装新时代耀眼火花
         * cate_name : 家居建材
         */

        public String id;
        public String uid;
        public String cate_id;
        public String logo;
        public String title;
        public String sign;
        public String cate_name;
    }
}
