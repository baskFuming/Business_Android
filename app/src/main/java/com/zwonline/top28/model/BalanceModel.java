package com.zwonline.top28.model;



import android.content.Context;

import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.WithdrawRecordBean;
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
     * 描述：提现和提现记录
     * @author YSG
     * @date 2018/1/10
     */
public class BalanceModel {
    private SharedPreferencesUtils sp;
    //提现
    public Flowable<HeadBean>Withdraw(Context context,int amount, String bank_card_id) throws IOException {
        sp= SharedPreferencesUtils.getUtil();
        String token= (String) sp.getKey(context,"dialog","");
        long timestamp=new Date().getTime();//时间戳
        Map<String,String>map=new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token",token);
        map.put("amount", String.valueOf(amount));
        map.put("bank_card_id",bank_card_id);
        SignUtils.removeNullValue(map);
        String sign= SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<HeadBean>flowable= ApiRetrofit.getInstance()
                .getClientApi(ApiService.class,Api.url)
                .iWithdraw(String.valueOf(timestamp),token,sign,amount,bank_card_id);
        return flowable;
    }
    //提现记录
    public Flowable<WithdrawRecordBean>withdrawRecord(Context context,String page) throws IOException {
        sp=SharedPreferencesUtils.getUtil();
        String token= (String) sp.getKey(context,"dialog","");
        long timestamp=new Date().getTime();//时间戳
        Map<String,String>map=new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token",token);
        map.put("page",page);
        SignUtils.removeNullValue(map);
        String sign= SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<WithdrawRecordBean>flowable=ApiRetrofit.getInstance()
                .getClientApi(ApiService.class,Api.url)
                .iWithdrawRecord(String.valueOf(timestamp),token,sign,page);
            return flowable;
    }
}
