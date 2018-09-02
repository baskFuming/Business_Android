package com.zwonline.top28.utils;

import android.content.Context;

import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.zwonline.top28.activity.WithoutCodeLoginActivity;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.PayService;
import com.zwonline.top28.bean.NoticeNotReadCountBean;
import com.zwonline.top28.bean.SettingBean;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class CustomNetData {
    //验证验证码
    public static void yanZhengApi2(Context context, final GT3GeetestUtilsBind gt3GeetestUtils, String geetest_challenge, String geetest_validate, String geetest_seccode, String seesionid ) {
        try {
            long timestamp = new Date().getTime() / 1000;//获取时间戳
            Map<String, String> map = new HashMap<>();
            map.put("timestamp", String.valueOf(timestamp));
            map.put("geetest_challenges", geetest_challenge);
            map.put("geetest_validates", geetest_validate);
            map.put("geetest_seccodes", geetest_seccode);
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<SettingBean> flowable = ApiRetrofit.getInstance()
                    .getClientApis(PayService.class, Api.url,seesionid)
                    .iGtValidate(String.valueOf(timestamp), geetest_challenge, geetest_validate, geetest_seccode, sign);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<SettingBean>() {

                        @Override
                        public void onNext(SettingBean yanzheng) {
//                            ToastUtils.showToast(WithoutCodeLoginActivity.this,yanzheng.msg+yanzheng.status);
                            if (yanzheng.status == 1) {
                                gt3GeetestUtils.gt3TestFinish();
                            } else {
                                gt3GeetestUtils.gt3TestClose();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {


                        }

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
