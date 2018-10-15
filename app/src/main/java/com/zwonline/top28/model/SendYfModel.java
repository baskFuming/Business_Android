package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.HongBaoLeftCountBean;
import com.zwonline.top28.bean.HongBaoLogBean;
import com.zwonline.top28.bean.SendYFBean;
import com.zwonline.top28.bean.YfRecordBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

public class SendYfModel {
    private SharedPreferencesUtils sp;

    /**
     * 发红包
     *
     * @param context
     * @param postscript
     * @param total_amount
     * @param total_package
     * @param random_flag
     * @return
     * @throws IOException
     */
    public Flowable<SendYFBean> sendYf(Context context, String postscript, String total_amount, String total_package, int random_flag) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("postscript", postscript);
        map.put("total_amount", total_amount);
        map.put("total_package", total_package);
        map.put("random_flag", String.valueOf(random_flag));
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<SendYFBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .sendHongbao(String.valueOf(timestamp), token, postscript, total_amount, total_package, random_flag, sign);
        return flowable;
    }


    /**
     * 红包记录
     *
     * @param context
     * @param hongbao_id
     * @param page
     * @return
     * @throws IOException
     */
    public Flowable<HongBaoLogBean> snatchYangFen(Context context, String hongbao_id, int page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("hongbao_id", hongbao_id);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("page", String.valueOf(page));
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<HongBaoLogBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .hongbaoLog(String.valueOf(timestamp), token, hongbao_id, page, sign);
        return flowable;
    }

    /**
     * 红包剩余量
     *
     * @param context
     * @param hongbao_id
     * @return
     * @throws IOException
     */
    public Flowable<HongBaoLeftCountBean> hongBaoLeftCount(Context context, String hongbao_id) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("hongbao_id", hongbao_id);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<HongBaoLeftCountBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .hongbaoLeftCount(String.valueOf(timestamp), token, hongbao_id, sign);
        return flowable;
    }

    /**
     * 总共抢到的红包记录
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<YfRecordBean> yfHongBaoRecord(Context context, int page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("page", String.valueOf(page));
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<YfRecordBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .yFRecord(String.valueOf(timestamp), token, page, sign);
        return flowable;
    }

}
