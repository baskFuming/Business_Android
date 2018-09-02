package com.zwonline.top28.model;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.zwonline.top28.R;
import com.zwonline.top28.api.PayService;
import com.zwonline.top28.bean.NoticeNotReadCountBean;
import com.zwonline.top28.bean.UserInfoBean;
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
 * @author YSG
 * @desc获取个人信息
 * @date ${Date}
 */
public class UserInfoModel {
    private SharedPreferencesUtils sp;

    /**
     * 个人信息
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<UserInfoBean> UserInfo(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        String token = (String) sp.getKey(context, "dialog", "");
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(context, R.string.user_not_login, Toast.LENGTH_SHORT).show();
        } else {
            map.put("token", token);
        }
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<UserInfoBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iUserInfo(String.valueOf(timestamp), token, sign);
        return flowable;
    }


    public Flowable<NoticeNotReadCountBean> NoticeNotReadCount(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        String token = (String) sp.getKey(context, "dialog", "");
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(context, R.string.user_not_login, Toast.LENGTH_SHORT).show();
        } else {
            map.put("token", token);
        }
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<NoticeNotReadCountBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iNotReadCount(String.valueOf(timestamp), token, sign);
        return flowable;
    }
}
