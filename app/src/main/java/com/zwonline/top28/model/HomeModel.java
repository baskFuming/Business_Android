package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.bean.HomeBean;
import com.zwonline.top28.bean.HomeClassBean;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.ApiService;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.ToastUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by YU on 2017/12/9.
 */

public class HomeModel {
    private String showAd = "1";
    private SharedPreferencesUtils sp;

    //首页
    public Flowable<HomeClassBean> homePage(Context context, String page, String cate_id) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        long timestamp = new Date().getTime() / 1000;//时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("page", page);
        map.put("show_ad", showAd);
        map.put("cate_id", cate_id);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<HomeClassBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iHomePage(page, cate_id, String.valueOf(timestamp), token, showAd, versionName, sign);
        return flowable;
    }

    //首页推荐
    public Flowable<HomeClassBean> homePage(Context context, String page) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        long timestamp = new Date().getTime() / 1000;//时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
//        page=1;
        map.put("page", page);
        map.put("show_ad", showAd);
        map.put("token", token);
        map.put("app_version", versionName);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<HomeClassBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iHomeRecommend(page, String.valueOf(timestamp), token, showAd,versionName, sign);
        return flowable;
    }

    //搜索
    public Flowable<HomeClassBean> Search(Context context, String title, String page) throws IOException {
        long timestamp = new Date().getTime() / 1000;//时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("page", page);
        map.put("title", title);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<HomeClassBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iSearch(title, page, String.valueOf(timestamp), token, sign);
        return flowable;
    }

    //首页分类
    public Flowable<HomeBean> homeClass(Context context) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<HomeBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iHomeClass(String.valueOf(timestamp), token,versionName, sign);
        return flowable;
    }

}
