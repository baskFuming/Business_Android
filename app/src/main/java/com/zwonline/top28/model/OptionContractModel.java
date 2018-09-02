package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.PayService;
import com.zwonline.top28.bean.OptionContractBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * @author YSG
 * @desc选择合同
 * @date ${Date}
 */
public class OptionContractModel {
    private SharedPreferencesUtils sp;

    /**
     * 选择合同
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<OptionContractBean> optionContract(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iOptionContract(String.valueOf(timestamp),token,sign);
    }

    /**
     * 选择合同模板
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<OptionContractBean> optionContracts(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        String is_official_template="1";
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("is_official_template",is_official_template);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iOptionContracts(String.valueOf(timestamp),token,sign,is_official_template);
    }
}
