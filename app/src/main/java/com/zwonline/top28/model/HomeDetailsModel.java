package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.ApiService;
import com.zwonline.top28.api.PayService;
import com.zwonline.top28.bean.AddCommentBean;
import com.zwonline.top28.bean.ArticleCommentBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.HomeDetailsBean;
import com.zwonline.top28.bean.PersonageInfoBean;
import com.zwonline.top28.bean.ShareDataBean;
import com.zwonline.top28.bean.UserBean;
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

public class HomeDetailsModel {
    private SharedPreferencesUtils sp;

    //文章详情
    public Flowable<HomeDetailsBean> homeDetail(Context context, int id) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("id", String.valueOf(id));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<HomeDetailsBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iHomeDetails(id, String.valueOf(timestamp), token, sign);
        return flowable;
    }

    //文章详情
    public Flowable<HomeDetailsBean> homeDetails(Context context, int id) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("id", String.valueOf(id));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<HomeDetailsBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iHomeDetail(id, String.valueOf(timestamp), sign);
        return flowable;
    }

    //分享文章
    public Flowable<ShareDataBean> shareData(Context context, String article_id) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("article_id", article_id);
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<ShareDataBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iSharData(article_id, token, String.valueOf(timestamp), sign);
        return flowable;
    }

    //添加评论
    public Flowable<AddCommentBean> addComment(Context context, String article_id, String content) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
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
                .AddComment(article_id, content, String.valueOf(timestamp), token, sign);
        return flowable;
    }

    //回复评论
    public Flowable<AddCommentBean> replyComment(Context context, String article_id, String content, String pid,String ppid) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("article_id", article_id);
        map.put("content", content);
        map.put("token", token);
        map.put("pid", pid);
        map.put("ppid", ppid);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AddCommentBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .ReplyComment(article_id, content, String.valueOf(timestamp), token, sign, ppid,pid);
        return flowable;
    }

    //收藏
    public Flowable<AddCommentBean> collect(Context context, String article_id, String fType) throws IOException {
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("article_id", article_id);
        map.put("type", fType);
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AddCommentBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .Collect(article_id, fType, String.valueOf(timestamp), token, sign);
        return flowable;
    }

    //关注
    public Flowable<AttentionBean> attention(Context context, String type, String uid, String allow_be_call) throws IOException {
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("type", type);
        map.put("token", token);
        map.put("uid", uid);
        map.put("allow_be_call", allow_be_call);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iAttention(token, String.valueOf(timestamp), sign, type, uid, allow_be_call);
        return flowable;
    }

    //取消关注
    public Flowable<AttentionBean> UnAttention(Context context, String type, String uid) throws IOException {
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("type", type);
        map.put("token", token);
        map.put("uid", uid);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iAttention(token, String.valueOf(timestamp), sign, type, uid);
        return flowable;
    }

    //个人中心信息
    public Flowable<PersonageInfoBean> Company(Context context, String uid) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("uid", uid);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<PersonageInfoBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iUser(String.valueOf(timestamp), token, sign, uid);
        return flowable;
    }


    /**
     * //文章评论
     *
     * @param context
     * @param article_id
     * @param page
     * @return
     * @throws IOException
     */
    public Flowable<ArticleCommentBean> articleComment(Context context, String article_id,String comment_id,String author_id,String sort_by,int page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("article_id", String.valueOf(article_id));
        map.put("comment_id", comment_id);
        map.put("author_id", author_id);
        map.put("sort_by", sort_by);
        map.put("page", String.valueOf(page));
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<ArticleCommentBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iArticleComment(String.valueOf(timestamp), token, article_id, comment_id,author_id,sort_by, page, sign);
        return flowable;
    }
}
