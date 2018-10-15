package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.bean.ProjectBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/** 我的项目列表
 * Created by sdh on 2018/3/8.
 */

public class ProjectModel {

    private SharedPreferencesUtils sp;

    public Flowable<ProjectBean> projectList(Context context, String uid) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        return ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .isProjectList(String.valueOf(timestamp), token,uid,
                        SignUtils.getSignature(map, Api.PRIVATE_KEY));
    }


}
