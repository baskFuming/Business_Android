package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.RecommendTeamsBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * 群标签的model
 */
public class GroupTagsModel {
    private SharedPreferencesUtils sp;

    /**
     * 热门标签
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<RecommendTeamsBean> mGroupTags(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<RecommendTeamsBean> flowablea = ApiRetrofit.getInstance().getClientApi(PayService.class, Api.url)
                .groupTags(String.valueOf(timestamp), token, sign);
        return flowablea;
    }

    /**
     * 添加标签
     *
     * @param context
     * @param name
     * @return
     * @throws IOException
     */
    public Flowable<AttentionBean> mAddTeamTag(Context context, String name) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("name", name);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AttentionBean> flowablea = ApiRetrofit.getInstance().getClientApi(PayService.class, Api.url)
                .addTeamTag(String.valueOf(timestamp), token, name, sign);
        return flowablea;
    }
}
