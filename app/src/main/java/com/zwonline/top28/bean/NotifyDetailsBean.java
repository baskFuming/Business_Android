package com.zwonline.top28.bean;

/**
 * 公告详情的Bean
 */
public class NotifyDetailsBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"title":"公告标题1","resume":"公告简述1","content":"公告详细内容","date":"2018/06/21 02:21"}
     * dialog : kksh5ngsdhejqueacqtn027f20
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * title : 公告标题1
         * resume : 公告简述1
         * content : 公告详细内容
         * date : 2018/06/21 02:21
         */

        public String title;
        public String resume;
        public String content;
        public String date;
    }
}
