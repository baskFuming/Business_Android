package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.BalanceBean;
import com.zwonline.top28.bean.BalancePayBean;
import com.zwonline.top28.bean.IntegralPayBean;
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
 * 积分充值
 * Created by sdh on 2018/3/13.
 */

public class IntegralPayModel {

    public Flowable<IntegralPayBean> getAmountByPoints(Context context, String rechType, String amount) throws IOException {
        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("amount", String.valueOf(amount));
        map.put("type", rechType);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iPointRecharge(String.valueOf(timestamp), token, rechType, amount,
                        SignUtils.getSignature(map, Api.PRIVATE_KEY));
    }

    /**
     * 根据积分获取金额接口
     *
     * @param context
     * @param points
     * @return
     * @throws IOException
     */
    public Flowable<AmountPointsBean> pointsRecharge(Context context, String points) throws IOException {
        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("points", points);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iGetAmountByPoints(String.valueOf(timestamp), token, points,
                        SignUtils.getSignature(map, Api.PRIVATE_KEY));
    }

//    /**
//     *
//     * @param context
//     * @param amount
//     * @param type
//     * @return
//     * @throws IOException
//     */
//    public Flowable<IntegralPayBean> getAmountByPoints(Context context, String type,String amount) throws IOException {
//        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
//        String token = (String) sp.getKey(context, "dialog", "");
//        long timestamp = new Date().getTime() / 1000;//时间戳
//        Map<String, String> map = new HashMap<>();
//        map.put("token", token);
//        map.put("timestamp", String.valueOf(timestamp));
//        map.put("amount", amount);
//        map.put("type", type);
//        return ApiRetrofit.getInstance()
//                .getClientApi(PayService.class, Api.url)
//                .iPointRecharge(String.valueOf(timestamp), token, amount,type,
//                        SignUtils.getSignature(map, Api.PRIVATE_KEY));
//    }

    /**
     * 获取支付宝orderStr
     *
     * @param context
     * @param orderId
     * @return
     * @throws IOException
     */
    public Flowable<PrepayPayBean> getOrderInfoById(Context context, String orderId) throws IOException {
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
                .iGetOrderAipayInfo(String.valueOf(timestamp), token, orderId, sign);
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

    /**
     * 获取订单详情
     * @param context
     * @param orderId
     * @return
     * @throws IOException
     */
    public Flowable<OrderInfoBean> getOrderInfo(Context context, String orderId) throws IOException {
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
                .iGetOrderInfo(String.valueOf(timestamp),token,orderId, sign);
    }

    //余额
    public Flowable<BalanceBean> Balance(Context context) throws IOException {
        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
        String token= (String) sp.getKey(context,"dialog","");
        final long timestamp=new Date().getTime()/1000;//时间戳
        Map<String,String>map=new HashMap<>();
        map.put("token",token);
        map.put("timestamp",String.valueOf(timestamp));
        final String sign= SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<BalanceBean> flowable= ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iBalance(String.valueOf(timestamp),token,sign);
        return flowable;
    }

    /**
     * 获取订单详情
     * @param context
     * @param amount
     * @return
     * @throws IOException
     */
    public Flowable<BalancePayBean> getBalancePay(Context context, String amount) throws IOException {
        SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("amount", amount);
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        return ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iBalancePay(String.valueOf(timestamp),token,amount, sign);
    }
}
