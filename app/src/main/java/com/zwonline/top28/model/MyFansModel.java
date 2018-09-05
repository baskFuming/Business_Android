package com.zwonline.top28.model;


import android.content.Context;

import com.zwonline.top28.api.PayService;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.MyFansBean;
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
 * 描述：我的粉丝
 *
 * @author YSG
 * @date 2017/12/22
 */
public class MyFansModel {
    private SharedPreferencesUtils sp;

    //个人主页的粉丝
    public Flowable<MyFansBean> mFans(Context context, String uid, int page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("uid", uid);
        map.put("page", String.valueOf(page));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<MyFansBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iFans(String.valueOf(timestamp), token, sign, uid, page);
        return flowable;
    }

    //我的粉丝
    public Flowable<MyFansBean> mMyFanses(Context context, String filter, int page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("page", String.valueOf(page));
        map.put("token", token);
        map.put("filter", filter);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<MyFansBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iMyFanses(String.valueOf(timestamp), token, sign, page, filter);
        return flowable;
    }

    //我的粉丝
    public Flowable<MyFansBean> mMyFans(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<MyFansBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iMyFans(String.valueOf(timestamp), token, sign);
        return flowable;
    }


    /**
     * 关注
     *
     * @param context
     * @param type
     * @param uid
     * @param allow_be_call
     * @return
     * @throws IOException
     */
    public Flowable<AttentionBean> attention(Context context, String type, String uid, String allow_be_call) throws IOException {
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("type", type);
        map.put("token", token);
        map.put("uid", uid);
        map.put("allow_be_call", allow_be_call);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iAttention(token, String.valueOf(timestamp), sign, type, uid, allow_be_call);
        return flowable;
    }


    //取消关注
    public Flowable<AttentionBean> UnAttention(Context context, String type, String uid) throws IOException {
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("type", type);
        map.put("token", token);
        map.put("uid", uid);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iAttention(token, String.valueOf(timestamp), sign, type, uid);
        return flowable;
    }
}
