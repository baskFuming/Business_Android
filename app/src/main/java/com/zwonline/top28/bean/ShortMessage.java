package com.zwonline.top28.bean;

import java.util.List;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/7 16:30
 */

public class ShortMessage {


    /**
     * status : 1
     * msg : 发送成功
     * data : []
     * dialog : ved0j4h2pafp5pfrqkl8gnjqi1
     */

    private int status;
    private String msg;
    private String dialog;
    private List<?> data;

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

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
