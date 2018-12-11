package com.zwonline.top28.wxapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zwonline.top28.R;

/**
 * Author     wildma
 * DATE       2017/07/16
 * Des	      ${友盟分享工具类}
 */
public class ShareUtils {

    /**
     * 分享链接
     */
    public static void shareWeb(final Activity activity, String WebUrl, String title, String description, String imageUrl, int imageID, SHARE_MEDIA platform) {
        UMWeb web = new UMWeb(WebUrl);//连接地址
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        if (TextUtils.isEmpty(imageUrl)) {
//            web.setThumb(new UMImage(activity, imageID));  //本地缩略图
            UMImage umImage = new UMImage(activity,imageID);
            umImage.compressFormat = Bitmap.CompressFormat.PNG;
            web.setThumb(umImage);

        } else {
            UMImage umImage = new UMImage(activity,imageUrl);
            umImage.compressFormat = Bitmap.CompressFormat.PNG;
            web.setThumb(umImage);  //网络缩略图
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
                                    Toast.makeText(activity,  activity.getString(R.string.share_fail), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity,  activity.getString(R.string.share_suc), Toast.LENGTH_SHORT).show();
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

    public void shareArticleData(){

    }

}
