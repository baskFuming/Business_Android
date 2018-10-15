package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.api.service.YangShiService;
import com.zwonline.top28.bean.MyPageBean;
import com.zwonline.top28.bean.YSBannerBean;
import com.zwonline.top28.bean.YSListBean;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * 鞅市M层
 */
public class YangShiModel {
    private SharedPreferencesUtils sp;

    /**
     * 鞅市轮播banner
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<YSBannerBean> mBannerList(Context context) throws IOException {
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
        Flowable<YSBannerBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(YangShiService.class, Api.url)
                .bannerList(String.valueOf(timestamp), token, versionName, sign);
        return flowable;
    }

    public Flowable<YSListBean> mAuctionList(Context context, String filter, int page) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        String token = (String) sp.getKey(context, "dialog", "");
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("app_version", versionName);
        map.put("filter", filter);
        map.put("page", String.valueOf(page));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<YSListBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(YangShiService.class, Api.url)
                .auctionList(String.valueOf(timestamp), token, versionName,filter,page, sign);
        return flowable;
    }
}
