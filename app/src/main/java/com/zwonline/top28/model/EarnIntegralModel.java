package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.PayService;
import com.zwonline.top28.bean.EarnIntegralBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * @author YSG
 * @desc赚取积分
 * @date ${Date}
 */
public class EarnIntegralModel {
    /**
     * 赚取积分的model
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<EarnIntegralBean> showIntegralList(Context context) throws IOException {
        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iEarnIntegral(String.valueOf(timestamp), token,
                        SignUtils.getSignature(map, Api.PRIVATE_KEY));
    }
}
