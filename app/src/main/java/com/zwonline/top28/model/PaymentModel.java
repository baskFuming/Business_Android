package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.bean.PrepayPayBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by sdh on 2018/3/12.
 * 确认支付model
 */

public class PaymentModel {
    private SharedPreferencesUtils sp;

    /**
     * 获取支付宝orderStr
     * @param context
     * @param orderId
     * @return
     * @throws IOException
     */
    public Flowable<PrepayPayBean> getOrderInfoById(Context context, String orderId) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("order_id", orderId);
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iGetOrderAipayInfo(String.valueOf(timestamp),token,orderId, sign);
    }

    /**
     * 获取订单详情
     * @param context
     * @param orderId
     * @return
     * @throws IOException
     */
    public Flowable<OrderInfoBean> getOrderInfo(Context context, String orderId) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("order_id", orderId);
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iGetOrderInfo(String.valueOf(timestamp),token,orderId, sign);
    }

    /**
     * 获取订单支付状态接口
     * @param context
     * @param orderId
     * @return
     * @throws IOException
     */
    public Flowable<AmountPointsBean> GetOrderPayStatus(Context context, String orderId) throws  IOException{
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("order_id", orderId);
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iGetOrderPayStatus(String.valueOf(timestamp),token,orderId, sign);
    }
}
