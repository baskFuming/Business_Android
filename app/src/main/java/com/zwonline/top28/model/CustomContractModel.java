package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.AddClauseBean;
import com.zwonline.top28.bean.AddContractBean;
import com.zwonline.top28.bean.SignContractBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * @author YSG
 * @desc生成合同
 * @date ${Date}
 */
public class CustomContractModel {
    private SharedPreferencesUtils sp;

    /**
     * 生成条款
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<AddClauseBean> customContract(Context context, String contract_id) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("contract_id", contract_id);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);//除去空值
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iContractDetail(String.valueOf(timestamp), token, sign, contract_id);
    }

    /**
     * 生成合同
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<AddContractBean> addContract(Context context, String project_id, String template_id, String title
            , String begin_date, String end_date, String terms) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("project_id", project_id);
        map.put("template_id", template_id);
        map.put("title", title);
        map.put("begin_date", begin_date);
        map.put("end_date", end_date);
        map.put("terms", terms);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);//除去空值
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iAddContact(String.valueOf(timestamp), token, sign, project_id, template_id, title, begin_date, end_date, terms);
    }
    /**
     * 扫码签署合同
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<SignContractBean> signContract(Context context, String orderId) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("order_id", orderId);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);//除去空值
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iSignContact(String.valueOf(timestamp), token, sign,orderId);
    }
}
