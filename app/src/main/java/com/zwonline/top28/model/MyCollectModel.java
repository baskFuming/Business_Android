package com.zwonline.top28.model;


import android.content.Context;

import com.zwonline.top28.bean.MyCollectBean;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.ApiService;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;


/**
 * 描述：我的收藏
 *
 * @author YSG
 * @date 2017/12/24
 */
public class MyCollectModel {
    private SharedPreferencesUtils sp;

    //收藏
    public Flowable<MyCollectBean> myConnect(Context context, String uid, int page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("uid", uid);
        map.put("page", String.valueOf(page));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<MyCollectBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iMyCollect(String.valueOf(timestamp), token, sign, uid, page);
        return flowable;
    }
}
