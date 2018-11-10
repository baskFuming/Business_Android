package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.bean.BindWechatBean;
import com.zwonline.top28.bean.RegisterRedPacketsBean;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * 设置绑定手机号或者微信的model
 */
public class BindWechatPhoneModel {

    private SharedPreferencesUtils sp;

    //绑定微信
    public Flowable<BindWechatBean> bindWechat(Context context, String union_id, String open_id, String gender
            , String nickname, String avatar, String country_code, String city, String province, String country, String language) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("union_id", union_id);
        map.put("open_id", open_id);
        map.put("gender", gender);
        map.put("nickname", nickname);
        map.put("avatar", avatar);
        map.put("country_code", country_code);
        map.put("city", city);
        map.put("province", province);
        map.put("country", country);
        map.put("language", language);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<BindWechatBean> flowable = ApiRetrofit.getInstance().getClientApi(ApiService.class, Api.url)
                .bindWx(union_id, open_id, gender, nickname, avatar, country_code, city, province, country, language, String.valueOf(timestamp), token, sign);
        return flowable;
    }


    /**
     * 弹窗接口
     *
     * @param context
     * @param type
     * @return
     * @throws IOException
     */
    public Flowable<RegisterRedPacketsBean> mShowDialog(Context context, String type) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", type);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<RegisterRedPacketsBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .showDialog(String.valueOf(timestamp), token, versionName, type, sign);
        return flowable;
    }

}
