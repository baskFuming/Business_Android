package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.ApiService;
import com.zwonline.top28.api.BusinessCircleService;
import com.zwonline.top28.api.PayService;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.HongbaoPermissionBean;
import com.zwonline.top28.bean.UnclaimedMbpCountBean;
import com.zwonline.top28.bean.UpdateCodeBean;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

public class MainModel {
    private SharedPreferencesUtils sp;

    /**
     * 版本自动更新
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<UpdateCodeBean> updataVersion(Context context, String platform, String version_code) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("platform", platform);
        map.put("version_code", version_code);
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<UpdateCodeBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iupdataVersion(String.valueOf(timestamp), token, sign, platform, versionName, version_code);
        return flowable;
    }

    /**
     * 红包权限
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<HongbaoPermissionBean> hBaoPermission(Context context) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<HongbaoPermissionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .hongBaoPermission(String.valueOf(timestamp), token, versionName, sign);
        return flowable;
    }

    /**
     * 鞅分未领取
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<UnclaimedMbpCountBean> claimedMbpCount(Context context) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<UnclaimedMbpCountBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .unclaimedMbpCount(String.valueOf(timestamp), token, versionName, sign);
        return flowable;
    }

}
