package com.zwonline.top28.bean;


import java.util.List;

/**
 * 描述：
 * @author YSG
 * @date 2017/12/12
 */
public class HotExamineBean {
    public String title;
    public int image;
    /**
     * status : 1
     * msg : success
     * data : [{"enterprise_name":"豌豆垃圾处理器","uid":"45","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1ba1aee8f9b.png_thumb.png","num":"5"},{"enterprise_name":"商机头条","uid":"1","logo":"https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png","num":"4"},{"enterprise_name":"御膏房","uid":"88","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bda161cf39.png_thumb.png","num":"2"},{"enterprise_name":"玫可儿美容护肤","uid":"78","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bc9743d05b.png_thumb.png","num":"1"},{"enterprise_name":"闺秘内衣","uid":"34","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1b7e097a0e6.png_thumb.png","num":"1"},{"enterprise_name":"美屋定制","uid":"37","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1b83101cac3.png_thumb.png_thumb.png","num":"1"},{"enterprise_name":"衬衣控","uid":"77","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bc9a79f907.png_thumb.png","num":[]},{"enterprise_name":"渝煮江湖","uid":"46","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1ba04862d1f.png_thumb.png","num":[]},{"enterprise_name":"宾客莱","uid":"96","logo":"https://toutiao.28.com/data/comp_logo/2.jpeg","num":[]},{"enterprise_name":"美家美邦","uid":"59","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bb881a885d.png_thumb.png","num":[]},{"enterprise_name":"记忆大师","uid":"62","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bb7c1d175b.png_thumb.png","num":[]},{"enterprise_name":"黛诗菲尔","uid":"79","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bc96d02817.png_thumb.png","num":[]},{"enterprise_name":"智能揽客系统","uid":"98","logo":"https://toutiao.28.com/data/upload/personal_avatars//171206/5a2755bc7e1a9.png_thumb.png","num":[]},{"enterprise_name":"移康智能猫眼","uid":"63","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bb97101ab9.png_thumb.png","num":[]},{"enterprise_name":"开心哈乐","uid":"47","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1ba78daa77f.png_thumb.png","num":[]},{"enterprise_name":"中网教育培训","uid":"113","logo":"https://toutiao.28.com/data/upload/personal_avatars//171213/5a30bb86597ef.png_thumb.png","num":[]},{"enterprise_name":"童蕴学堂","uid":"64","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bba64a33a0.png_thumb.png","num":[]},{"enterprise_name":"熊奈儿","uid":"80","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bcc1406f39.png_thumb.png","num":[]},{"enterprise_name":"欧亿优品","uid":"48","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1ba4e3b4dd3.png_thumb.png","num":[]},{"enterprise_name":"洗得一站式汽车服务","uid":"65","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bbc3d23837.png_thumb.png","num":[]},{"enterprise_name":"汤姆之家","uid":"33","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1b79b2d63ec.png_thumb.png","num":[]},{"enterprise_name":"众孝中老年生活馆","uid":"81","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bcccba2543.png_thumb.png","num":[]},{"enterprise_name":"邻家儿女","uid":"50","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1ba5a84a003.png_thumb.png","num":[]},{"enterprise_name":"荣事达智能洗衣","uid":"66","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bbde5bbee4.png_thumb.png","num":[]},{"enterprise_name":"美丽心情饰品","uid":"22","logo":"https://toutiao.28.com/data/comp_logo/5.jpeg","num":[]},{"enterprise_name":"卤三国","uid":"49","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1ba591b5daa.png_thumb.png","num":[]},{"enterprise_name":"新艺代艺术","uid":"67","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bbd508683d.png_thumb.png","num":[]},{"enterprise_name":"洁当家","uid":"32","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1b84539fd75.png_thumb.png","num":[]},{"enterprise_name":"纤思韵产后修复","uid":"82","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1bd08e21039.png_thumb.png","num":[]},{"enterprise_name":"涂奈克墙面大师","uid":"51","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1ba785a698f.png_thumb.png","num":[]}]
     * dialog : ved0j4h2pafp5pfrqkl8gnjqi1
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * enterprise_name : 豌豆垃圾处理器
         * uid : 45
         * logo : https://toutiao.28.com/data/upload/personal_avatars//171127/5a1ba1aee8f9b.png_thumb.png
         * num : 5
         */

        public String enterprise_name;
        public String uid;
        public String logo;
        public String num;
    }
}
