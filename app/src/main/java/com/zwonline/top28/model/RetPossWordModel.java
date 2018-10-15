package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.bean.LoginBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * @author YSG
 * @desc密码重置
 * @date ${Date}
 */
public class RetPossWordModel {
    private SharedPreferencesUtils sp;

    //密码重置
    public Flowable<SettingBean> RetPoss(Context context, String password) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        String token = (String) sp.getKey(context, "dialog", "");
        map.put("token", token);
        map.put("password", password);
        map.put("timestamp", String.valueOf(timestamp));
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<SettingBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iRetPossword(String.valueOf(timestamp), token, sign, password);
        return flowable;
    }

    //账号密码登录
    public Flowable<LoginBean> loginUserNumber(Context context, String mobile, String password) throws IOException {
        long timestamp = new Date().getTime() / 1000;//时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("password", password);
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<LoginBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .loginUserNumber(mobile, password, String.valueOf(timestamp), token, sign);
        return flowable;
    }

    //获取个人信息
    public Flowable<UserInfoBean> UserInfo(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        String token = (String) sp.getKey(context, "dialog", "");
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token",token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<UserInfoBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iUserInfo(String.valueOf(timestamp), token, sign);
        return flowable;
    }
}
