package com.zwonline.top28.api.exception;

import com.zwonline.top28.constants.Constant;

/**
 * 异常处理的一个类
 * Created by sdh on 2018/3/20.
 */

public class ApiException extends RuntimeException {

    private int mErrorCode;

    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }

    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
    public boolean isTokenExpried() {
        return mErrorCode == Constant.EXCEPTION_TOKEN;
    }
}
