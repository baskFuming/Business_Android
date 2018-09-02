package com.zwonline.top28.bean;

import java.util.List;

public class SignContractBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"id":"64","user_id":"210","project_id":"111","title":"自定义004","begin_date":"2018-04-19","end_date":"2018-04-19","is_template":"0","is_official_template":"0","is_delete":"0","create_time":"2018-04-19 11:07:39","update_time":"0000-00-00 00:00:00","terms":[{"id":"155","contract_id":"64","title":"好吧QQ群","content":"我的人都有一群卿卿我我","percent":"100","is_delete":"0","create_time":"2018-04-19 11:07:39"},{"id":"156","contract_id":"64","title":"好吧QQ群","content":"我的人都有一群卿卿我我","percent":"100","is_delete":"0","create_time":"2018-04-19 11:07:39"}],"enterprise_points":"75.332440","insurance_pool_points":"0"}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * id : 64
         * user_id : 210
         * project_id : 111
         * title : 自定义004
         * begin_date : 2018-04-19
         * end_date : 2018-04-19
         * is_template : 0
         * is_official_template : 0
         * is_delete : 0
         * create_time : 2018-04-19 11:07:39
         * update_time : 0000-00-00 00:00:00
         * terms : [{"id":"155","contract_id":"64","title":"好吧QQ群","content":"我的人都有一群卿卿我我","percent":"100","is_delete":"0","create_time":"2018-04-19 11:07:39"},{"id":"156","contract_id":"64","title":"好吧QQ群","content":"我的人都有一群卿卿我我","percent":"100","is_delete":"0","create_time":"2018-04-19 11:07:39"}]
         * enterprise_points : 75.332440
         * insurance_pool_points : 0
         */

        public String id;
        public String user_id;
        public String project_id;
        public String title;
        public String begin_date;
        public String end_date;
        public String is_template;
        public String is_official_template;
        public String is_delete;
        public String create_time;
        public String update_time;
        public String enterprise_points;
        public String insurance_pool_points;
        public List<TermsBean> terms;

        public static class TermsBean {
            /**
             * id : 155
             * contract_id : 64
             * title : 好吧QQ群
             * content : 我的人都有一群卿卿我我
             * percent : 100
             * is_delete : 0
             * create_time : 2018-04-19 11:07:39
             */

            public String id;
            public String contract_id;
            public String title;
            public String content;
            public String percent;
            public String is_delete;
            public String create_time;
        }
    }
}
