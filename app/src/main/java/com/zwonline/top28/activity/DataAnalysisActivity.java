package com.zwonline.top28.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据分析展示
 */

public class DataAnalysisActivity extends AppCompatActivity {
    private SharedPreferencesUtils sp;
    private ProgressBar progressBar;
    private WebView datawebView;
    private String token;
    private String url = Api.baseUrl() + "/Members/chart.html";
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_analysis);
        Intent intent =getIntent();
        uid = intent.getStringExtra("uid");
        sp = SharedPreferencesUtils.getUtil();
        initView();
    }

    //found ID
    private void initView() {
        token = (String) sp.getKey(this, "dialog", "");
        String cookieString = "PHPSESSID=" + token + "; path=/";
        synCookies(url, cookieString);
        progressBar = (ProgressBar) findViewById(R.id.progress_Bar);
        datawebView = (WebView) findViewById(R.id.data_web);
        webSettingInit();
    }

    // WebView展示
    private void webSettingInit() {
        WebSettings settings = datawebView.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString("app28/");
        settings.setSupportZoom(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
        settings.setDomStorageEnabled(true);//设置适应Html5的一些方法
        //请求头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Accept-Language", LanguageUitils.getCurCountryLan());
        datawebView.loadUrl(url, headMap);
        datawebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (url.contains("http://top28app//pushToUserHomepage/")) {
                    String path = "http://top28app//pushToUserHomepage/";
                    String uid = url.substring(path.length(), url.length());
                    Intent intent1 = new Intent(DataAnalysisActivity.this, HomePageActivity.class);
                    intent1.putExtra("uid", uid);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }
                //判断用户单击的是那个超连接
                String tag = "tel";
                if (url.contains(tag)) {
                    String mobile = url.substring(url.lastIndexOf("/") + 1);
                    Intent mIntent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse(mobile);
                    mIntent.setData(data);
                    //Android6.0以后的动态获取打电话权限
                    if (ActivityCompat.checkSelfPermission(DataAnalysisActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(mIntent);
                        //这个超连接,java已经处理了，webview不要处理
                        return true;
                    } else {
                        //申请权限
                        ActivityCompat.requestPermissions(DataAnalysisActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        return true;
                    }
                }
                return false;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        datawebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
    }
    /**
     * 将cookie同步到WebView
     * @param url          WebView要加载的url
     * @param cookieString 要同步的cookie
     * @return true 同步cookie成功，false同步cookie失败
     * @Author JPH
     */
    protected void synCookies(String url, String cookieString) {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.removeAllCookie();  //移除所有
        cookieManager.setCookie(url, cookieString);//cookies是在HttpClient中获得的cookie 如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        CookieSyncManager.getInstance().sync();
    }
}
