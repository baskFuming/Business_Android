package com.zwonline.top28.api.subscriber;

/**
 * Created by sdh on 2018/3/15.
 */

public interface SubscriberOnNextListener<T> {

    void onNext(T t);
}
