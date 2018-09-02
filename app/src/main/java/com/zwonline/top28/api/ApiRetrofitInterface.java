package com.zwonline.top28.api;

import android.content.Context;

/**
 * 1. 类的用途
 * 2. @author forever
 * 3. @date 2017/5/13 10:34
 */

public interface ApiRetrofitInterface {

    //返回ApiService
    <T> T getClientApis(Class<T> cla, String url,String seesionid);
    //返回ApiService
    <T> T getClientApi(Class<T> cla, String url);
}
