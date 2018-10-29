package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.IntegralBean;
import com.zwonline.top28.bean.MyCurrencyBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * @author YSG
 * @desc 积分
 * @date 2017-12-25
 */
public class MyCurrencyModel {
    private SharedPreferencesUtils sp;

    //我的创业币
    public Flowable<MyCurrencyBean> myCurrency(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<MyCurrencyBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iMyCurrency(String.valueOf(timestamp), token, sign);
        return flowable;
    }

    /**
     * 商机币查询
     *
     * @param context
     * @param type
     * @param page
     * @return
     * @throws IOException
     */
    public Flowable<IntegralBean> mBalanceLog(Context context, String type, int page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("type", type);
        map.put("page", String.valueOf(page));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<IntegralBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iBalanceLog(String.valueOf(timestamp), token, page, type, sign);
        return flowable;
    }
}
