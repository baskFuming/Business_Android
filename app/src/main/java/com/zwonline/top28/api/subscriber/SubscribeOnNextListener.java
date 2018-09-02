package com.zwonline.top28.api.subscriber;

/**
 * Created by sdh on 2018/3/20.
 */

public interface SubscribeOnNextListener<T> {
    void onNext(T t);
    void onError(Throwable e);
}
