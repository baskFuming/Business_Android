package com.zwonline.top28.model;


import android.content.Context;

import com.zwonline.top28.bean.BalanceBean;
import com.zwonline.top28.bean.PaymentBean;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.ApiService;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
     * 描述：收付款记录   余额显示
     * @author YSG
     * @date 2017/12/27
     */
public class WalletModel {
    private SharedPreferencesUtils sp;

    //收付款记录
    public Flowable<PaymentBean> payMent(Context context,String page) throws IOException {
        sp= SharedPreferencesUtils.getUtil();
        String token= (String) sp.getKey(context,"dialog","");
        final long timestamp=new Date().getTime()/1000;//时间戳
        Map<String,String>map=new HashMap<>();
        map.put("token",token);
        map.put("page",page);
        map.put("timestamp",String.valueOf(timestamp));
        final String sign= SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<PaymentBean> flowable= ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iPayment(String.valueOf(timestamp),token,sign,page);
        return flowable;
    }
    //余额
    public Flowable<BalanceBean> Balance(Context context) throws IOException {
        sp= SharedPreferencesUtils.getUtil();
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
}
