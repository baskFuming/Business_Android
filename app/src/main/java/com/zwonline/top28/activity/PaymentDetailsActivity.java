package com.zwonline.top28.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.CookieSyncManager;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
     * 描述：订单详情
     * @author YSG
     * @date 2018/2/6
     */
public class PaymentDetailsActivity extends AppCompatActivity {

    private RelativeLayout back;
    private TextView collect;
    private ProgressBar pay_progress_Bar;
    private WebView pay_web;
    private SharedPreferencesUtils sp;
    private String token;
    public String url = Api.baseUrl()+"/Members/view_order.html?order_id=436";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        initView();
    }

    private void initView() {
        Intent intent=getIntent();
        String order_id=intent.getStringExtra("order_id");
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        String cookieString = "PHPSESSID=" + token + "; path=/";
//        CookieManager.getInstance().setCookie("https://toutiao.28.com/Members/view_order.html?"+order_id, cookieString);
        synCookies(Api.baseUrl()+"/Members/view_order.html?"+order_id, cookieString);
        back = (RelativeLayout) findViewById(R.id.back);
        collect = (TextView) findViewById(R.id.collect);
        pay_progress_Bar = (ProgressBar) findViewById(R.id.pay_progress_Bar);
        pay_web = (WebView) findViewById(R.id.pay_web);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
            }
        });

    }

    /**
     * 将cookie同步到WebView
     * @param url WebView要加载的url
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
    //webview配置
    public void webSetingDaya(){
        WebSettings settings = pay_web.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString("app28/");
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);//设置适应Html5的一些方法
        //请求头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Accept-Language", LanguageUitils.getCurCountryLan());
        pay_web.loadUrl(url, headMap);

        pay_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pay_progress_Bar.setVisibility(View.GONE);
                } else {
                    pay_progress_Bar.setVisibility(View.VISIBLE);
                    pay_progress_Bar.setProgress(newProgress);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(PaymentDetailsActivity.this);
//                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

            //设置响应js 的Confirm()函数
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(PaymentDetailsActivity.this);
                b.setTitle("Confirm");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                b.create().show();
                return true;
            }

        });
    }

}
