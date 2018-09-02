package com.zwonline.top28.bean;

import java.util.List;

/**
 * 公告列表的Bean
 */
public class AnnouncementBean {


    /**
     * status : 1
     * msg : 成功
     * data : [{"id":"1","title":"关于熬夜计划1111111111","resume":"koasjdojasionda","date":"2018/06/21 02:21"},{"id":"5","title":"nkliasnodnaso","resume":"olasnodnasonm","date":"2018/06/21 01:35"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 1
         * title : 关于熬夜计划1111111111
         * resume : koasjdojasionda
         * date : 2018/06/21 02:21
         * "is_read": "1"//已经读取
         */

        public String notice_id;
        public String title;
        public String resume;
        public String date;
        public String is_read;
    }
}
