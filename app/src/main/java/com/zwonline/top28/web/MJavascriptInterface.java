package com.zwonline.top28.web;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.zwonline.top28.activity.PhotoBrowserActivity;

/**
 * 文章详情抓取图片跳转查看大图
 */
public class MJavascriptInterface {
    private Activity context;
    private static final String TAG = "SIMON";

    public MJavascriptInterface(Activity context) {
        this.context = context;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img, String[] array) {
        Log.i(TAG, "openImage: " + img);
        if (!img.contains("sjtt_adblock")) {
            Intent intent = new Intent(context, PhotoBrowserActivity.class);
            intent.putExtra("imageUrls", array);
            intent.putExtra("curImg", img);
            context.startActivity(intent);
        }

    }

}
