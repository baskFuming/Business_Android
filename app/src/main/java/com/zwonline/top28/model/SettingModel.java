package com.zwonline.top28.model;


import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.ApiService;
import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.IndustryBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 描述：个人设置
 *
 * @author YSG
 * @date 2017/12/28
 */
public class SettingModel {
    private SharedPreferencesUtils sp;

    //上传头像
    public Flowable<HeadBean> SettingHead(Context context, File file) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        //多个参数
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("timestamp", String.valueOf(timestamp))
                .addFormDataPart("token", token)
                .addFormDataPart("sign", sign)
                .addFormDataPart("file", file.getName(), imageBody);
        List<MultipartBody.Part> parts = builder.build().parts();

        Flowable<HeadBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iHead(parts);
        return flowable;
    }


    //个人设置资料  weixin email telephone job_cate_pid
    public Flowable<SettingBean> mSetingModel(Context context, String nick_name,
                                              String real_name, int sex, String age,
                                              String address, String favourite_industry,
                                              String bio,String weixin,String email,String telephone,String job_cate_pid
                                       ,String enterprise,String position) throws IOException {

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
                .getClientApi(ApiService.class, Api.url)
                .iSetting(String.valueOf(timestamp), token, sign, nick_name, real_name, sex, age, address, favourite_industry, bio
                ,weixin,email,telephone,job_cate_pid,enterprise,position);
        return flowable;
    }

    //感兴趣行业分类
    public Flowable<IndustryBean> mIndustry(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        final long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));

        final String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<IndustryBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .Industry(String.valueOf(timestamp), token, sign);
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
                .getClientApi(ApiService.class, Api.url)
                .iUserInfo(String.valueOf(timestamp),token, sign);
        return flowable;
    }


}
