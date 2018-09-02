package com.zwonline.top28.model;


import android.content.Context;

import com.zwonline.top28.bean.MyExamine;
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
 * 描述：我的考察Model
 *
 * @author YSG
 * @date 2017/12/21
 */
public class MyExamineModel {
    private SharedPreferencesUtils sp;

    //我的考察
    public Flowable<MyExamine> myExamine(Context context,int page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token",token);
        map.put("page", String.valueOf(page));
        System.out.print("tokenxx=="+token);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<MyExamine> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iMyExamine(String.valueOf(timestamp), token, sign,page);
        return flowable;
    }
}
