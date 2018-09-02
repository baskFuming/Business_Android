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
import com.zwonline.top28.adapter.MyBillAdapter;
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
 * 描述：我的账单
 *
 * @author YSG
 * @date 2017/12/26
 */
public class MyBillActivity extends BaseActivity {


    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.back_xx)
    RelativeLayout backXx;
    @BindView(R.id.my_bill_tv)
    TextView myBillTv;
    @BindView(R.id.bill_progress_Bar)
    ProgressBar billProgressBar;
    @BindView(R.id.mybill_web)
    WebView mybillWeb;
    private MyBillAdapter adapter;
    private SharedPreferencesUtils sp;
    private String token;
    public String url = Api.baseUrl()+"/Members/bill.html";

    @Override
    protected void init() {
//        presenter.mMyBill(MyBillActivity.this);
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        String cookieString = "PHPSESSID=" + token + "; path=/";
        synCookies(url, cookieString);
//        CookieManager.getInstance().setCookie(url, cookieString);
        WebSettings settings = mybillWeb.getSettings();
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
        mybillWeb.loadUrl(url, headMap);
        mybillWeb.setWebViewClient(new WebViewClient() {
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

        mybillWeb.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    billProgressBar.setVisibility(View.GONE);
                } else {
                    billProgressBar.setVisibility(View.VISIBLE);
                    billProgressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                myBillTv.setText(title);
                if (mybillWeb.canGoBack()) {
                    backXx.setVisibility(View.VISIBLE);
                } else {
                    backXx.setVisibility(View.GONE);

                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(MyBillActivity.this);
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
                AlertDialog.Builder b = new AlertDialog.Builder(MyBillActivity.this);
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
        return R.layout.activity_my_bill;
    }


    @OnClick({R.id.back, R.id.back_xx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (mybillWeb.canGoBack()) {
                    mybillWeb.goBack();// 返回前一个页面
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
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mybillWeb.canGoBack()) {
            mybillWeb.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mybillWeb.stopLoading();
        mybillWeb.removeAllViews();
        mybillWeb.destroy();
        mybillWeb = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
