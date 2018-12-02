package com.zwonline.top28.model;

import android.content.Context;
import android.util.Log;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.bean.LoginBean;
import com.zwonline.top28.bean.LoginWechatBean;
import com.zwonline.top28.bean.ShortMessage;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/9 14:14
 */

public class LoginModel {
    private SharedPreferencesUtils sp;

    //账号密码登录
    public Flowable<LoginBean> loginUserNumber(Context context, String mobile, String password) throws IOException {
        long timestamp = new Date().getTime() / 1000;//时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("password", password);
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<LoginBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .loginUserNumber(mobile, password, String.valueOf(timestamp), token, sign);
        return flowable;
    }

    //获取手机验证码
    public Flowable<ShortMessage> getPhoneCode(String phone, String type, String token, String country_code) {
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("type", type);
        map.put("country_code", country_code);
        SignUtils.removeNullValue(map);
        try {
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<ShortMessage> flowable = ApiRetrofit.getInstance().getClientApis(ApiService.class, Api.url, token).getPhoneCode(phone, String.valueOf(timestamp), type, country_code, sign);
            return flowable;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //验证忘记密码手机验证码
    public Flowable<LoginBean> forGetPossword(Context context, String phone, String code, String token) {
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("smscode", code);
        map.put("token", token);
        SignUtils.removeNullValue(map);
        try {
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<LoginBean> flowable = ApiRetrofit.getInstance().getClientApi(ApiService.class, Api.url).iforgetPossword(String.valueOf(timestamp), sign, phone, code, token);
            return flowable;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取手机验证码登录
    public Flowable<LoginBean> loginUserVerify(Context context, String mobile, String shortmsg, String dialog) throws IOException {
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("smscode", shortmsg);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", dialog);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<LoginBean> flowable = ApiRetrofit.getInstance().getClientApi(ApiService.class, Api.url).loginUserVerify(mobile, shortmsg, String.valueOf(timestamp), sign, dialog);
        return flowable;
    }

    //获取手机验证码登录
    public Flowable<LoginBean> loginUserVerifys(Context context, String mobile, String shortmsg, String dialog, String incode) throws IOException {
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("smscode", shortmsg);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", dialog);
        map.put("incode", incode);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<LoginBean> flowable = ApiRetrofit.getInstance().getClientApi(ApiService.class, Api.url).loginUserVerifys(mobile, shortmsg, String.valueOf(timestamp), sign, incode, dialog);
        return flowable;
    }
    //city province country language
    //微信授权登录
    public Flowable<LoginWechatBean> loginWechat(Context context, String union_id, String open_id, String gender
            , String nickname, String avatar, String country_code,String city,String province,String country,String language) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("union_id", union_id);
        map.put("open_id", open_id);
        map.put("gender", gender);
        map.put("nickname", nickname);
        map.put("avatar", avatar);
        map.put("country_code", country_code);
        map.put("city",city);
        map.put("province",province);
        map.put("country",country);
        map.put("language",language);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<LoginWechatBean> flowable = ApiRetrofit.getInstance().getClientApi(ApiService.class, Api.url)
                .loginWechat(union_id, open_id, gender, nickname, avatar, country_code,city,province,country,language, String.valueOf(timestamp), token,sign);
        return flowable;
    }
    //登录云信
    public void doLogin(String account, String token) {

        NimUIKit.login(new LoginInfo(account, token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                Log.i("----------------", "login success");

            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }


}
