package com.zwonline.top28.activity;

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
 * 描述：推广服务
 *
 * @author YSG
 * @date 2018/1/3
 */
public class GeneralizeActivity extends BaseActivity {


    @BindView(R.id.generalize_web)
    WebView generalizeWeb;
    private SharedPreferencesUtils sp;
    private String token;
    private String cookieString = "PHPSESSID=" + token + "; path=/";
    private String url = Api.baseUrl() + "/Members/app_store.html";

    @Override
    protected void init() {
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        WebSettings settings = generalizeWeb.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);// 设置支持缩放
        settings.setJavaScriptEnabled(true);//支持JS
        generalizeWeb.setWebViewClient(new WebViewClient());//禁止跳系统浏览器
//        CookieManager.getInstance().setCookie(url, cookieString);
        synCookies(url, cookieString);
        generalizeWeb.getSettings().setUserAgentString("app28/");
        settings.setSupportZoom(true);
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
        //请求头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Accept-Language", LanguageUitils.getCurCountryLan());
        generalizeWeb.loadUrl(url, headMap);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_generalize;
    }

}
