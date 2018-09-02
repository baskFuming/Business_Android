package com.zwonline.top28.model;


import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.bean.AddBankBean;
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
 * 描述：添加银行卡
 * @author YSG
 * @date 2017/12/26
 */
public class AddBankModel {
    private SharedPreferencesUtils sp;
    public Flowable<AddBankBean> addBank(Context context, String card_holder, String card_number, String card_bank) throws IOException {
        sp= SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token= (String) sp.getKey(context,"dialog","");
        Map<String,String> map=new HashMap<>();
        map.put("token",token);
        map.put("timestamp",String.valueOf(timestamp));
        map.put("card_holder",card_holder);
        map.put("card_number",card_number);
        map.put("card_bank",card_bank);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<AddBankBean>flowable= ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iAddBank(String.valueOf(timestamp),token,sign,card_holder,card_number,card_bank);
        return flowable;
    }
}
