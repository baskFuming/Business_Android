package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.bean.HotExamineBean;
import com.zwonline.top28.bean.VideoBean;
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
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class HotExamineModel {
    private SharedPreferencesUtils sp;
    //热门考察
    public  Flowable<HotExamineBean> hotExamine(Context context,int page) throws IOException {
        long timestamp =new Date().getTime()/1000;//获取时间戳
        Map<String,String> map=new HashMap<>();
        map.put("page",String.valueOf(page));
        map.put("timestamp",String.valueOf(timestamp));
        String sign= SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<HotExamineBean>flowable= ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iHotExamine(String.valueOf(page),String.valueOf(timestamp),sign);
        return flowable;
    }
    //视频
    public  Flowable<VideoBean> videoList(Context context,String page,String cate_id) throws IOException {
        long timestamp =new Date().getTime()/1000;//获取时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String,String> map=new HashMap<>();
        map.put("page",String.valueOf(page));
        map.put("cate_id",cate_id);
        map.put("token",token);
        map.put("timestamp",String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign= SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<VideoBean>flowable= ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iVideo(cate_id,String.valueOf(timestamp),token,sign,page);
        return flowable;
    }
    //视频推荐
    public  Flowable<VideoBean> videoList(Context context,String page) throws IOException {
        long timestamp =new Date().getTime()/1000;//获取时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String,String> map=new HashMap<>();
        map.put("page",String.valueOf(page));
        map.put("timestamp",String.valueOf(timestamp));
        map.put("token",token);
        SignUtils.removeNullValue(map);
        String sign= SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<VideoBean>flowable= ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iVideoRecommennt(String.valueOf(timestamp),token,sign,page);
        return flowable;
    }
}
