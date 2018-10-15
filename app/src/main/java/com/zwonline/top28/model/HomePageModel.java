package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.api.service.BusinessCircleService;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.ExamineChatBean;
import com.zwonline.top28.bean.InforNoticeBean;
import com.zwonline.top28.bean.InforNoticeCleanBean;
import com.zwonline.top28.bean.MyIssueBean;
import com.zwonline.top28.bean.MyShareBean;
import com.zwonline.top28.bean.PersonageInfoBean;
import com.zwonline.top28.bean.RealBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.TipBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * @author YSG
 * @desc个人主页
 * @date ${Date}
 */
public class HomePageModel {
    private SharedPreferencesUtils sp;

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
    public Flowable<AttentionBean> unFollowAttention(Context context, String type, String uid) throws IOException {
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

    //我的发布
    public Flowable<MyIssueBean> myIssue(Context context, String uid, int page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("uid", uid);
        map.put("page", String.valueOf(page));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<MyIssueBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iMyIssue(String.valueOf(timestamp), token, sign, uid, page);
        return flowable;
    }

    //我的分享
    public Flowable<MyShareBean> MyShare(Context context, String uid, int page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("uid", uid);
        map.put("page", String.valueOf(page));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<MyShareBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iMyShare(String.valueOf(timestamp), token, sign, uid, page);
        return flowable;
    }

    //检查是否和某人聊过天接口
    public Flowable<ExamineChatBean> ExamineChat(Context context, String uid) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("uid", uid);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<ExamineChatBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iExamineChat(String.valueOf(timestamp), token, uid, sign);
        return flowable;
    }


    //在线聊天
    public Flowable<AmountPointsBean> OnLineChat(Context context, String uid) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("uid", uid);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AmountPointsBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iOnLineChated(String.valueOf(timestamp), token, uid, sign);
        return flowable;
    }

    //通知列表
    public Flowable<InforNoticeBean> inForNotice(Context context, int page) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("app_version", versionName);
        map.put("page", String.valueOf(page));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<InforNoticeBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .InforNoticeList(String.valueOf(timestamp), token, sign, versionName, page);
        return flowable;
    }

    //清空通知列表
    public Flowable<InforNoticeCleanBean> inForNoticeClean(Context context, String page) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("app_version", versionName);
        map.put("page", page);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<InforNoticeCleanBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .InforNoticeCLeanList(String.valueOf(timestamp), token, sign, versionName, page);
        return flowable;
    }

    //是否已经读取
    public Flowable<TipBean> inForNoticeTip(Context context, int page) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("app_version", versionName);
        map.put("page", String.valueOf(page));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<TipBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .markNotificationRead(String.valueOf(timestamp), token, sign, versionName, page);
        return flowable;
    }

    //微信分享名片
    public Flowable<RealBean> shareWxin(Context context, String show_realname, String show_telephone, String show_weixin, String show_address
    ,String show_enterprise,String show_position) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        map.put("app_version", versionName);
        map.put("show_realname", show_realname);
        map.put("show_telephone", show_telephone);
        map.put("show_weixin", show_weixin);
        map.put("show_address", show_address);
        map.put("show_enterprise",show_enterprise);
        map.put("show_position",show_position);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<RealBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .sjPageCallback(String.valueOf(timestamp), token, sign, versionName, show_realname,
                        show_telephone,show_weixin,show_address,show_enterprise,show_position);
        return flowable;
    }
    //微信名片更新  weixin email telephone job_cate_pid
    public Flowable<SettingBean> mSetingModel(Context context, String nick_name,
                                              String real_name, int sex, String age,
                                              String address, String favourite_industry,
                                              String bio, String weixin, String email, String telephone, String job_cate_pid,String enterprise,String position) throws IOException {

        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("nick_name", nick_name);
        map.put("real_name", real_name);
        map.put("sex", String.valueOf(sex));
        map.put("age", String.valueOf(age));
        map.put("address", address);
        map.put("favourite_industry", String.valueOf(favourite_industry));
        map.put("bio", bio);
        map.put("weixin", weixin);
        map.put("email", email);
        map.put("telephone", telephone);
        map.put("job_cate_pid", job_cate_pid);
        map.put("enterprise",enterprise);
        map.put("position",position);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<SettingBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iSetting(String.valueOf(timestamp), token, sign, nick_name, real_name, sex, age, address, favourite_industry, bio
                        ,weixin,email,telephone,job_cate_pid,enterprise,position);
        return flowable;
    }

    //个人信息
    public Flowable<UserInfoBean> UserInfo(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<UserInfoBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iUserInfo(String.valueOf(timestamp),token, sign);
        return flowable;
    }

}
