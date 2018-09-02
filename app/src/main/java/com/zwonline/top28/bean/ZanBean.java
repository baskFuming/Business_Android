package com.zwonline.top28.bean;

/**
 * 点赞的bean
 */
public class ZanBean {

    /**
     * status : 1
     * msg : 成功
     * data : {"newest_zan_count":"1"}
     * dialog : ee66fb27fc77339d5c6c2cb420894753
     */

    private int status;
    private String msg;
    private DataBean data;
    private String dialog;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public static class DataBean {
        /**
         * newest_zan_count : 1
         */

        private String newest_zan_count;

        public String getNewest_zan_count() {
            return newest_zan_count;
        }

        public void setNewest_zan_count(String newest_zan_count) {
            this.newest_zan_count = newest_zan_count;
        }
    }
}
