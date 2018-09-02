package com.zwonline.top28.bean;


import java.util.List;

/**
     * 描述：我的考察bean
     * @author YSG
     * @date 2017/12/20
     */
public class MyExamine {

    /**
     * status : 1
     * msg : success
     * data : [{"id":"2","num":"4","enterprise_uid":"1","enterprise_address_id":"3","subscribe_date":"2017-11-25","review_status":"1","enterprise_name":"商机头条","logo":"https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png","area":"北京市海淀区","address":"闵庄路玉泉慧谷","mobile":"010-60846618-2816"},{"id":"1","num":"4","enterprise_uid":"1","enterprise_address_id":"4","subscribe_date":"2017-11-25","review_status":"1","enterprise_name":"商机头条","logo":"https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png","area":"上海市","address":"上海市","mobile":"010-60846618-2816"},{"id":"7","num":"4","enterprise_uid":"1","enterprise_address_id":"3","subscribe_date":"2017-11-27","review_status":"2","enterprise_name":"商机头条","logo":"https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png","area":"北京市海淀区","address":"闵庄路玉泉慧谷","mobile":"010-60846618-2816"},{"id":"6","num":"1","enterprise_uid":"37","enterprise_address_id":"14","subscribe_date":"2017-11-27","review_status":"2","enterprise_name":"美屋定制","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1b83101cac3.png_thumb.png_thumb.png","area":"中国","address":"北京市通州区宋庄镇宋庄创意工场街","mobile":"010-60846618-2816"},{"id":"5","num":"5","enterprise_uid":"45","enterprise_address_id":"15","subscribe_date":"2017-11-27","review_status":"2","enterprise_name":"豌豆垃圾处理器","logo":"https://toutiao.28.com/data/upload/personal_avatars//171127/5a1ba1aee8f9b.png_thumb.png","area":"中国","address":"江苏高新区会展西路88号2号楼1-1005","mobile":"010-60846618-2816"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 2
         * num : 4
         * enterprise_uid : 1
         * enterprise_address_id : 3
         * subscribe_date : 2017-11-25
         * review_status : 1
         * enterprise_name : 商机头条
         * logo : https://toutiao.28.com/data/upload/personal_avatars//171125/5a195bd1066b7.png_thumb.png
         * area : 北京市海淀区
         * address : 闵庄路玉泉慧谷
         * mobile : 010-60846618-2816
         */

        public String id;
        public String num;
        public String enterprise_uid;
        public String enterprise_address_id;
        public String subscribe_date;
        public String review_status;
        public String enterprise_name;
        public String logo;
        public String area;
        public String address;
        public String mobile;
    }
}
