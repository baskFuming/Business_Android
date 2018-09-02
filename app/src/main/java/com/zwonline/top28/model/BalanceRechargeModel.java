package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.PayService;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.BalanceRechargeBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * 余额充值
 * Created by sdh on 2018/3/14.
 */

public class BalanceRechargeModel {

    private SharedPreferencesUtils sp;

    //余额充值 支付宝
    public Flowable<BalanceRechargeBean> walletBalanceRecharge(Context context, Double amount,
                                                               String rechargeType) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime();//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("amount", String.valueOf(amount));
        map.put("type", rechargeType);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iBalanceRecharge(String.valueOf(timestamp), token, amount, rechargeType, sign);
    }

    //余额充值 银行卡
    public Flowable<AmountPointsBean> walletBalanceRecharges(Context context, Double amount,
                                                             String rechargeType) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime();//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("amount", String.valueOf(amount));
        map.put("type", rechargeType);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iBalanceRecharges(String.valueOf(timestamp), token, amount, rechargeType, sign);
    }

    /**
     * 获取订单详情
     *
     * @param context
     * @param orderId
     * @return
     * @throws IOException
     */
    public Flowable<OrderInfoBean> getOrderInfos(Context context, String orderId) throws IOException {
        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("order_id", orderId);
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iGetOrderInfo(String.valueOf(timestamp), token, orderId, sign);
    }


    /**
     * 返回积分单价接口
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<AmountPointsBean> mUnitPrice(Context context) throws IOException {
        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iUnitPrice(String.valueOf(timestamp), token, sign);
    }
}
