package com.zwonline.top28.wxapi;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.ApiService;
import com.zwonline.top28.api.BusinessCircleService;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.AttentionBean;
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
 * Author     wildma
 * DATE       2017/07/16
 * Des	      ${友盟分享工具类}
 */
public class ShareUtilses {

    /**
     * 分享链接
     */
    public static void shareWeb(final Activity activity, String WebUrl, String title, String description, String imageUrl, int imageID, SHARE_MEDIA platform, final String articleID) {
        UMWeb web = new UMWeb(WebUrl);//连接地址
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        if (TextUtils.isEmpty(imageUrl)) {
            web.setThumb(new UMImage(activity, imageID));  //本地缩略图
        } else {
            web.setThumb(new UMImage(activity, imageUrl));  //网络缩略图
        }
        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (share_media.name().equals("WEIXIN_FAVORITE")) {
                                    Toast.makeText(activity, activity.getString(R.string.share_fail), Toast.LENGTH_SHORT).show();
//                                    shareArticleData(articleID);
                                } else {
                                    Toast.makeText(activity, activity.getString(R.string.share_suc), Toast.LENGTH_SHORT).show();
                                    shareArticleData(activity, articleID);
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (throwable != null) {
                            Log.d("throw", "C:" + throwable.getMessage());
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, activity.getString(R.string.share_fail), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, activity.getString(R.string.share_cancel), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .share();


        //新浪微博中图文+链接
        /*new ShareAction(activity)
                .setPlatform(platform)
                .withText(description + " " + WebUrl)
                .withMedia(new UMImage(activity,imageID))
                .share();*/
    }

    /**
     * 分享链接
     */
    public static void shareWebs(final Activity activity, String WebUrl, String title, String description, String imageUrl, int imageID, SHARE_MEDIA platform, final String dynamicId) {
        UMWeb web = new UMWeb(WebUrl);//连接地址
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        if (TextUtils.isEmpty(imageUrl)) {
            web.setThumb(new UMImage(activity, imageID));  //本地缩略图
        } else {
            web.setThumb(new UMImage(activity, imageUrl));  //网络缩略图
        }
        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (share_media.name().equals("WEIXIN_FAVORITE")) {
                                    Toast.makeText(activity, activity.getString(R.string.share_fail), Toast.LENGTH_SHORT).show();
                                    shareDynamicData(activity,dynamicId);
                                } else {
                                    Toast.makeText(activity, activity.getString(R.string.share_suc), Toast.LENGTH_SHORT).show();
                                    shareDynamicData(activity,dynamicId);
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (throwable != null) {
                            Log.d("throw", "C:" + throwable.getMessage());
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, activity.getString(R.string.share_fail), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, activity.getString(R.string.share_cancel), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .share();
    }

    public static void shareArticleData(Context context, String id) {
        try {
            SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
            String token = (String) sp.getKey(context, "dialog", "");
            long timestamp = new Date().getTime() / 1000;//时间戳
            Map<String, String> map = new HashMap<>();
            map.put("timestamp", String.valueOf(timestamp));
            map.put("id", id);
            map.put("token", token);
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<AmountPointsBean> flowable = ApiRetrofit.getInstance()
                    .getClientApi(ApiService.class, Api.url)
                    .iShareSuccess(String.valueOf(timestamp), token, sign, id);
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

    public static void shareDynamicData(Context context, String id) {
        try {
            SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
            String token = (String) sp.getKey(context, "dialog", "");
            long timestamp = new Date().getTime() / 1000;//时间戳
            Map<String, String> map = new HashMap<>();
            map.put("timestamp", String.valueOf(timestamp));
            map.put("id", id);
            map.put("token", token);
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                    .getClientApi(BusinessCircleService.class, Api.url)
                    .iShareMomentSuccess(String.valueOf(timestamp), token, sign, id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean shareArticleBean) {

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
