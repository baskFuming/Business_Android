package com.zwonline.top28.utils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/*
   避免Intent之间传值太大造成Trancsation问题
   封装工具类
 */
public class WeakDataHolder {
    private Map<String, WeakReference<Object>> datalist = new HashMap<>();
    /*
      设置单例模式
     */
    private static  WeakDataHolder instance;

    public static WeakDataHolder getInstance() {
        if (instance == null) {
            synchronized (WeakDataHolder.class) {
                if (instance == null) {
                    instance = new WeakDataHolder();
                }
            }
        }
        return instance;
    }

    /*
     * 用作数据存储
     * @param key
     * @params value
     */
    public void setData(String key,Object value) {
        datalist.put(key,new WeakReference<Object>(value));
    }

    /*
      用作获得数据
      @params key
     */
    public Object getData(String key) {
        WeakReference weakReference = datalist.get(key);
        if (weakReference!=null){
            Object object = weakReference.get();
            return object;
        }
        return null;
    }
}
