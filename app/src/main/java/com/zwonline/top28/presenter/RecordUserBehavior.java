package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.PayService;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author YSG
 * @desc 记录用户行为
 * @date ${Date}
 */
public class RecordUserBehavior {

    public static void recordUserBehavior(Context context, String event) {
        try {
            SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
            String token = (String) sp.getKey(context, "dialog", "");
            long timestamp = new Date().getTime() / 1000;//时间戳
            Map<String, String> map = new HashMap<>();
            map.put("timestamp", String.valueOf(timestamp));
            map.put("token", token);
            map.put("event", event);
            SignUtils.removeNullValue(map);
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<AmountPointsBean> flowable = ApiRetrofit.getInstance()
                    .getClientApi(PayService.class, Api.url)
                    .iRecordUserBehavior(String.valueOf(timestamp), token, sign, event);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AmountPointsBean>() {
                        @Override
                        public void onNext(AmountPointsBean shareArticleBean) {

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

    public static void recordUserBehaviors(Context context, String event, String targetId) {
        try {
            SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
            String token = (String) sp.getKey(context, "dialog", "");
            long timestamp = new Date().getTime() / 1000;//时间戳
            Map<String, String> map = new HashMap<>();
            map.put("timestamp", String.valueOf(timestamp));
            map.put("token", token);
            map.put("event", event);
            map.put("target_id", targetId);
            SignUtils.removeNullValue(map);
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<AmountPointsBean> flowable = ApiRetrofit.getInstance()
                    .getClientApi(PayService.class, Api.url)
                    .iRecordUserBehaviors(String.valueOf(timestamp), token, sign, targetId, event);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AmountPointsBean>() {
                        @Override
                        public void onNext(AmountPointsBean shareArticleBean) {

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
