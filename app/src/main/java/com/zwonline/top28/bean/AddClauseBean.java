package com.zwonline.top28.bean;

import java.util.List;

/**
 * @author YSG
 * @desc添加条款的ben
 * @date ${Date}
 */
public class AddClauseBean {
    /**
     * data : {"begin_date":"","end_date":"","id":"1","is_official_template":"1","is_template":"0","terms":[{"content":"测试条款内容1","contract_id":"1","create_time":"2018-04-07 14:33:46","id":"1","percent":"30","title":"测试条款1"},{"content":"测试条款内容2","contract_id":"1","create_time":"2018-04-07 14:34:15","id":"2","percent":"30","title":"测试条款2"},{"content":"测试条款内容3","contract_id":"1","create_time":"2018-04-07 14:34:36","id":"3","percent":"40","title":"测试条款3"}],"title":"招商加盟模板"}
     * dialog :
     * msg : 成功
     * status : 1
     */

    public DataBean data;
    public String dialog;
    public String msg;
    public int status;

    public static class DataBean {
        /**
         * begin_date :
         * end_date :
         * id : 1
         * is_official_template : 1
         * is_template : 0
         * terms : [{"content":"测试条款内容1","contract_id":"1","create_time":"2018-04-07 14:33:46","id":"1","percent":"30","title":"测试条款1"},{"content":"测试条款内容2","contract_id":"1","create_time":"2018-04-07 14:34:15","id":"2","percent":"30","title":"测试条款2"},{"content":"测试条款内容3","contract_id":"1","create_time":"2018-04-07 14:34:36","id":"3","percent":"40","title":"测试条款3"}]
         * title : 招商加盟模板
         */

        public String begin_date;
        public String end_date;
        public String id;
        public String is_official_template;
        public String is_template;
        public String title;
        public List<TermsBean> terms;

        public static class TermsBean {
            public TermsBean(String title, String percent, String content) {
                this.title = title;
                this.percent = percent;
                this.content = content;
            }

            public TermsBean() {
            }

            /**
             * content : 测试条款内容1
             * contract_id : 1
             * create_time : 2018-04-07 14:33:46
             * id : 1
             * percent : 30
             * title : 测试条款1
             */

            public String content;
//            public String contract_id;
//            public String create_time;
//            public String id;
            public String percent;
            public String title;
        }
    }

//    public String clause_names;
//    public String clause_ratios;
//    public String clause_contents;
//
//    public AddClauseBean(String clause_names, String clause_ratios, String clause_contents) {
//        this.clause_names = clause_names;
//        this.clause_ratios = clause_ratios;
//        this.clause_contents = clause_contents;
//    }
//
//    public AddClauseBean() {
//
//    }

}
