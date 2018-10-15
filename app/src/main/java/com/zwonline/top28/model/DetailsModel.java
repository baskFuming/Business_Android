package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.AddCommentBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.DetailsBean;
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
 * Created by YU on 2017/12/11.
 */

public class DetailsModel {
    private SharedPreferencesUtils sp;
    //文章详情
    public Flowable<DetailsBean> Details(Context context, int id, String token) throws IOException {
        sp= SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("id", String.valueOf(id));
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<DetailsBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iDetails(id, String.valueOf(timestamp), token,sign);
        return flowable;
    }
    //添加评论
    public Flowable<AddCommentBean> addComment(Context context, String article_id, String content) throws IOException {
        sp= SharedPreferencesUtils.getUtil();
        String token= (String) sp.getKey(context,"dialog","");
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("article_id", article_id);
        map.put("content", content);
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AddCommentBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .AddComment(article_id,content, String.valueOf(timestamp),token, sign);
        return flowable;
    }

    //回复评论
    public Flowable<AddCommentBean> replyComment(Context context, String article_id, String content,String pid,String ppid) throws IOException {
        sp= SharedPreferencesUtils.getUtil();
        String token= (String) sp.getKey(context,"dialog","");
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("article_id", article_id);
        map.put("content", content);
        map.put("token", token);
        map.put("pid",pid);
        map.put("ppid",ppid);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AddCommentBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .ReplyComment(article_id,content, String.valueOf(timestamp),token, sign,ppid,pid);
        return flowable;
    }
    //收藏
    public Flowable<AddCommentBean> collect(Context context, String article_id,String fType) throws IOException {
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        sp= SharedPreferencesUtils.getUtil();
        String token= (String) sp.getKey(context,"dialog","");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("article_id", article_id);
        map.put("type",fType);
        map.put("token", token);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AddCommentBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .Collect(article_id, fType,String.valueOf(timestamp),token,sign);
        return flowable;
    }
    //关注/取消关注
    public Flowable<AttentionBean> attention(Context context, String type, String uid) throws IOException {
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        sp= SharedPreferencesUtils.getUtil();
        String token= (String) sp.getKey(context,"dialog","");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("type", type);
        map.put("token", token);
        map.put("uid", uid);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iAttention(token, String.valueOf(timestamp),sign,type,uid);
        return flowable;
    }
}
