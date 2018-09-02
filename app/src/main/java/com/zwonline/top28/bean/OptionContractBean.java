package com.zwonline.top28.bean;

import java.util.List;

/**
 * @author YSG
 * @desc选择合同bean
 * @date ${Date}
 */
public class OptionContractBean {

    /**
     * status : 1
     * msg : 成功
     * data : [{"contract_id":"1","project_id":"","title":"招商加盟模板"},{"contract_id":"2","project_id":"","title":"进货模板"},{"contract_id":"3","project_id":"","title":"品牌授权模板"}]
     * dialog :
     */

    public int status;
    public String msg;
    public String dialog;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * contract_id : 1
         * project_id :
         * title : 招商加盟模板
         */

        public String contract_id;
        public String project_id;
        public String title;
    }
}
