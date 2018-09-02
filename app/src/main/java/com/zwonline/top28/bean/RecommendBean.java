package com.zwonline.top28.bean;

import java.util.List;

/**
 * @author YSG
 * @desc商机推荐项目
 * @date ${Date}
 */
public class RecommendBean {

    /**
     * status : 1
     * msg : success
     * data : [{"id":"58","uid":"82","cate_id":"338","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bd08e21039.png_thumb.png","title":"纤思韵产后修复","sign":"产后恢复调理中心-展现辣妈曲线身型","cate_name":"美容保健"},{"id":"35","uid":"61","cate_id":"334","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bb68c35511.png_thumb.png","title":"洁希亚洗衣","sign":"更强净争力","cate_name":"环保机械"},{"id":"29","uid":"54","cate_id":"339","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bae1aea3ea.png_thumb.png","title":"VR主题公园","sign":"VR乐园 畅玩挣不停","cate_name":"特色项目"},{"id":"14","uid":"39","cate_id":"332","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1b8524586ba.png_thumb.png","title":"乔东家排骨大包","sign":"秘制排骨大包 2人开店挣","cate_name":"餐饮小吃"},{"id":"1","uid":"1","cate_id":"337","logo":"https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png","title":"商机头条","sign":"创业保险 签约付费","cate_name":"教育网络"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 58
         * uid : 82
         * cate_id : 338
         * logo : https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bd08e21039.png_thumb.png
         * title : 纤思韵产后修复
         * sign : 产后恢复调理中心-展现辣妈曲线身型
         * cate_name : 美容保健
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
