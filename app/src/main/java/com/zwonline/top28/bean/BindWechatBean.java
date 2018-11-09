package com.zwonline.top28.bean;

/**
 *  绑定微信Bean类
 */
public class BindWechatBean {

    /**
     * status : 1
     * msg : 成功
     * data : {}
     * dialog : 693o2cktcrrlf2b2eni620ghe1
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

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
    }
}
