package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.AddFriendBean;
import com.zwonline.top28.bean.RecommendTeamsBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * 搜索添加好友
 */
public class AddFriendModel {
    private SharedPreferencesUtils sp;

    /**
     * 搜索好友
     *
     * @param context
     * @param keyword
     * @return
     * @throws IOException
     */
    public Flowable<AddFriendBean> addFriend(Context context, String keyword) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("keyword", keyword);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AddFriendBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iAddFriend(String.valueOf(timestamp), token, keyword, sign);
        return flowable;
    }

    /**
     * 群推荐
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<RecommendTeamsBean> recommendTeamsBean(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<RecommendTeamsBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .recommendTeams(String.valueOf(timestamp), token, sign);
        return flowable;
    }
}
