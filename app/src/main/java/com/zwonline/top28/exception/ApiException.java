package com.zwonline.top28.exception;

/**
 * Created by sdh on 2018/3/9.
 * 定义回调异常类
 */

public class ApiException extends Exception {

    /**
     * 错误码
     * */
    private int code;
    /**
     *显示的信息
     */
    private String displayMessage;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;

    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public int getCode() {
        return code;
    }
}
