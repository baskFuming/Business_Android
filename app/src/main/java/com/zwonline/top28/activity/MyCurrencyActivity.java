package com.zwonline.top28.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：伯乐币
 *
 * @author YSG
 * @date 2017/12/25
 */
public class MyCurrencyActivity extends BaseActivity {
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.back_xx)
    RelativeLayout backXx;
    @BindView(R.id.currency_title)
    TextView currencyTitle;
    @BindView(R.id.currency_progress_Bar)
    ProgressBar currencyProgressBar;
    @BindView(R.id.current_web)
    WebView currentWeb;
    private SharedPreferencesUtils sp;
    private String token;
    public String url = Api.baseUrl() + "/Members/boc.html";
    private String cookieString;

    @Override
    protected void init() {
//        presenter.mMyCurrency(this);
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        cookieString = "PHPSESSID=" + token + "; path=/";
        synCookies(url, cookieString);
//        CookieManager.getInstance().setCookie(url, cookieString);
        WebSettings settings = currentWeb.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString("app28/");
        settings.setSupportZoom(true);
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
        settings.setDomStorageEnabled(true);//设置适应Html5的一些方法
        //请求头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Accept-Language", LanguageUitils.getCurCountryLan());
        currentWeb.loadUrl(url, headMap);
        currentWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
            }
        });


        currentWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                currencyTitle.setText(title);
                if (currentWeb.canGoBack()) {
                    backXx.setVisibility(View.VISIBLE);
                } else {
                    backXx.setVisibility(View.GONE);

                }
            }


            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(MyCurrencyActivity.this);
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


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    currencyProgressBar.setVisibility(View.GONE);
                } else {
                    currencyProgressBar.setVisibility(View.VISIBLE);
                    currencyProgressBar.setProgress(newProgress);
                }
            }

            //设置响应js 的Confirm()函数
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(MyCurrencyActivity.this);
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

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_currency;
    }


    @OnClick({R.id.back, R.id.back_xx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (currentWeb.canGoBack()) {
                    currentWeb.goBack();// 返回前一个页面
                } else {
                    finish();
                }
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.back_xx:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && currentWeb.canGoBack()) {
            currentWeb.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentWeb.stopLoading();
        currentWeb.removeAllViews();
        currentWeb.destroy();
        currentWeb = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
