package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.MyPageBean;
import com.zwonline.top28.bean.NoticeNotReadCountBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * @author YSG
 * @desc获取个人信息
 * @date ${Date}
 */
public class UserInfoModel {
    private SharedPreferencesUtils sp;

    /**
     * 个人信息
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<UserInfoBean> UserInfo(Context context) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        String token = (String) sp.getKey(context, "dialog", "");
        map.put("token", token);
        map.put("app_version", versionName);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<UserInfoBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iUserInfo(String.valueOf(timestamp), token, versionName, sign);
        return flowable;
    }

    /**
     * 通告
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<NoticeNotReadCountBean> NoticeNotReadCount(Context context) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        String token = (String) sp.getKey(context, "dialog", "");
        map.put("token", token);
        map.put("app_version", versionName);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<NoticeNotReadCountBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iNotReadCount(String.valueOf(timestamp), token, versionName, sign);
        return flowable;
    }

    /**
     * 个人中心菜单
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<MyPageBean> mPersonCenterMenu(Context context) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        String token = (String) sp.getKey(context, "dialog", "");
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<MyPageBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .personCenterMenu(String.valueOf(timestamp), token, versionName, sign);
        return flowable;
    }
}
