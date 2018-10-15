package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.bean.QrCodeBean;
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
 * @desc 生成二维码的model
 * @date ${Date}
 */
public class QrCodeModel {

    private SharedPreferencesUtils sp;

    public Flowable<QrCodeBean> Qrcode(Context context, Double amount, String projectId,String contractId) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("amount",String.valueOf( amount));
        map.put("project_id", projectId);
        map.put("contract_id", contractId);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iQrcode(String.valueOf(timestamp), token, sign, projectId,amount,contractId);

    }
}
