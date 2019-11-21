package com.zwonline.top28.base;

import java.lang.ref.WeakReference;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/6 13:42
 */

public abstract class BasePresenter<V > {

    public WeakReference weakReference=null;

    //关联View层
    public void attachView(V view){
        weakReference=new WeakReference(view);
    }
    //解除关联
    public void detachView(){
        if(weakReference!=null){
            weakReference.clear();
            weakReference=null;
        }
    }
    //得到view
    public V getView(){
        return (V) weakReference.get();
    }


}
