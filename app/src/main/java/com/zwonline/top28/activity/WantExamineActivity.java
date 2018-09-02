package com.zwonline.top28.activity;

import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 描述：我想考察
 *
 * @author YSG
 * @date 2018/4/20
 */
public class WantExamineActivity extends BaseActivity {

    @BindView(R.id.examine_web)
    WebView examineWeb;
    private SharedPreferencesUtils sp;
    private String uid;
    private String token;
    private String cookieString = "PHPSESSID=" + token + "; path=/";

    @Override
    protected void init() {
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        WebSettings settings = examineWeb.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);// 设置支持缩放
        settings.setJavaScriptEnabled(true);//支持JS
        examineWeb.setWebViewClient(new WebViewClient());//禁止跳系统浏览器
//        CookieManager.getInstance().setCookie("https://toutiao.28.com/Review/inspect/uid/" + uid + ".html", cookieString);
        synCookies(Api.baseUrl() + "/Review/inspect/uid/" + uid + ".html", cookieString);
        //请求头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Accept-Language", LanguageUitils.getCurCountryLan());
        examineWeb.loadUrl(Api.baseUrl() + "/Review/inspect/uid/" + uid + ".html", headMap);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_want_examine;
    }

}
