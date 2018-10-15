package com.zwonline.top28.model;


import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.bean.MyShareBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * 描述：我的分享
 *
 * @author YSG
 * @date 2017/12/25
 */
public class MyShareModel {
    //我的分享
    private SharedPreferencesUtils sp;

    //我的分享
    public Flowable<MyShareBean> MyShare(Context context, String uid, int page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("uid", uid);
        map.put("page", String.valueOf(page));
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<MyShareBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iMyShare(String.valueOf(timestamp), token, sign, uid, page);
        return flowable;
    }
}
