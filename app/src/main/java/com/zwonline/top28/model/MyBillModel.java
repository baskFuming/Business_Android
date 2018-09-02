package com.zwonline.top28.model;


import android.content.Context;
import android.util.Log;

import com.zwonline.top28.bean.MyBillBean;
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
     * 描述：我的账单
     * @author YSG
     * @date 2017/12/26
     */
public class MyBillModel {
    private SharedPreferencesUtils sp;

    //我的账单
    public Flowable<MyBillBean> myBill(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Log.e("signxxxx",sign);
        Log.e("signxxxx",token);
        Flowable<MyBillBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iMyBill(String.valueOf(timestamp), token, sign);
        return  flowable;
    }
}
