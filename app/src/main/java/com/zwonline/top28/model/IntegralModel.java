package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.IntegralBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by sdh on 2018/3/10.
 * 积分类
 */

public class IntegralModel {

    /**
     * 后台发送获取积分列表
     * @param context
     * @param integralType
     * @param page
     * @return
     * @throws IOException
     */
    public Flowable<IntegralBean> showIntegralList(Context context, String integralType, String page) throws IOException {
        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("page", page);
        map.put("type", integralType);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iIntegralList(String.valueOf(timestamp), token, page,integralType ,
                        SignUtils.getSignature(map, Api.PRIVATE_KEY));
    }

    public Flowable<IntegralBean> showAllIntegralList(Context context, String page) throws IOException {
        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("page", page);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iAllIntegralList(String.valueOf(timestamp), token, page, sign);
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
    public Flowable<IntegralBean> mBalanceRecord(Context context, String type, int page) throws IOException {
        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
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
