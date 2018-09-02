package com.zwonline.top28.model;


import android.content.Context;

import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.BankBean;
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
 * 描述：我的银行卡列表
 *
 * @author YSG
 * @date 2017/12/27
 */
public class BankModel {
    private SharedPreferencesUtils sp;

    //银行卡列表
    public Flowable<BankBean> BankList(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iBank(String.valueOf(timestamp), token, sign);
    }

    //银行卡解绑
    public Flowable<AmountPointsBean> UnbindBank(Context context,String id) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", id);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iUnbindBank(String.valueOf(timestamp), token, id,sign);
    }

}
