package com.zwonline.top28.model;

import android.content.Context;
import android.util.Log;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.ApiService;
import com.zwonline.top28.bean.LoginWechatBean;
import com.zwonline.top28.bean.RegisterBean;
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
 * 3.@date2017/12/7 15:38
 */

public class RegisterModel {
    private SharedPreferencesUtils sp;



    //获取手机验证码
    public Flowable<ShortMessage> getPhoneCode(String phone,String type,String token,String country_code){
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String,String> map=new HashMap<>();
        map.put("mobile",phone);
        map.put("timestamp",String.valueOf(timestamp));
        map.put("type",type);
        map.put("country_code",country_code);
        SignUtils.removeNullValue(map);
        try {
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<ShortMessage> flowable = ApiRetrofit.getInstance().getClientApis(ApiService.class, Api.url,token).getPhoneCode(phone,String.valueOf(timestamp),type,country_code,sign);
            return flowable;
        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
    }
    //注册用户
    public Flowable<RegisterBean> registerUser(Context context,String mobile, String smscode, String password, String passwordVerify, String token) throws IOException {
        long timestamp = new Date().getTime() / 1000;//时间戳
        RegisterBean bean=new RegisterBean();
        Map<String,String> map=new HashMap<>();
        map.put("mobile",mobile);
        map.put("token",token);
        map.put("timestamp",String.valueOf(timestamp));
        map.put("smscode",smscode);
        map.put("password",password);
        map.put("passwordVerify",passwordVerify);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<RegisterBean> flowable = ApiRetrofit.getInstance().getClientApi(ApiService.class, Api.url).registerUser(mobile,smscode,password,passwordVerify,String.valueOf(timestamp),sign,token);
        return flowable;
    }
    //微信授权登录
    public Flowable<LoginWechatBean> loginWechat(Context context, String union_id, String open_id, String gender
    ,String nickname,String avatar,String country_code) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("union_id", union_id);
        map.put("open_id", open_id);
        map.put("gender",gender);
        map.put("nickname",nickname);
        map.put("avatar",avatar);
        map.put("country_code",country_code);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("token", token);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<LoginWechatBean> flowable = ApiRetrofit.getInstance().getClientApi(ApiService.class, Api.url).loginWechat(
                union_id, open_id, gender,nickname,avatar,country_code,String.valueOf(timestamp),token,sign);
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
