package com.zwonline.top28.api.subscriber;

import com.zwonline.top28.api.exception.NetError;

/**
 * Created by sdh on 2018/3/19.
 */

public abstract class SubscriberListener<T> {
    public abstract void onSuccess(T response);

    public abstract void onFail(NetError error);

    public abstract void onError(Throwable e);

    public void onCompleted() {
    }

    public void onBegin() {
    }

    public boolean isShowLoading(){
        return true;
    }

    public abstract void checkReLogin(String errorCode,String errorMsg);
}
