package com.zwonline.top28.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
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

import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

/**
 * 描述：充值记录
 *
 * @author YSG
 * @date 2018/3/15
 */
public class RechargeRecordActivity extends BaseActivity {

    private RelativeLayout rechargeRecordBack;
    private RelativeLayout backPayXx;
    private ProgressBar bankProgressBar;
    private WebView rechageWeb;
    private SharedPreferencesUtils sp;
    private String token;
    public String url = Api.baseUrl() + "/Members/recharge_list.html";
    private String orderId;


    @Override
    protected void init() {
        initView();
        sp = SharedPreferencesUtils.getUtil();
//        Intent intent=getIntent();
//        orderId = intent.getStringExtra("orderCode");
        token = (String) sp.getKey(this, "dialog", "");
        String cookieString = "PHPSESSID=" + token + "; path=/";
        synCookies(url, cookieString);
        webViewInit();
    }


    private void initView() {
        rechargeRecordBack = (RelativeLayout) findViewById(R.id.recharge_record_back);
        backPayXx = (RelativeLayout) findViewById(R.id.back_pay_xx);
        bankProgressBar = (ProgressBar) findViewById(R.id.bank_progress_Bar);
        rechageWeb = (WebView) findViewById(R.id.rechage_web);

    }

    private void webViewInit() {
        WebSettings webSettings = rechageWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //可执行js
        webSettings.setUserAgentString("app28/");
        // 使用localStorage则必须打开
        webSettings.setDomStorageEnabled(true);
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        //请求头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Accept-Language", LanguageUitils.getCurCountryLan());
        rechageWeb.loadUrl(url, headMap);
        rechageWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bankProgressBar.setVisibility(View.GONE);
                } else {
                    bankProgressBar.setVisibility(View.VISIBLE);
                    bankProgressBar.setProgress(newProgress);
                }
            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (rechageWeb.canGoBack()) {
                    backPayXx.setVisibility(View.VISIBLE);
                } else {
                    backPayXx.setVisibility(View.GONE);

                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(RechargeRecordActivity.this);
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
                AlertDialog.Builder b = new AlertDialog.Builder(RechargeRecordActivity.this);
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

        rechageWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                ToastUtils.showToast(ArticleActivity.this,"url="+url);

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

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_recharge_record;
    }

    @OnClick({R.id.recharge_record_back, R.id.back_pay_xx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recharge_record_back:
                if (rechageWeb.canGoBack()) {
                    rechageWeb.goBack();// 返回前一个页面
                } else {
                    finish();
                }
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.back_pay_xx:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && rechageWeb.canGoBack()) {
            rechageWeb.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rechageWeb.stopLoading();
        rechageWeb.removeAllViews();
        rechageWeb.destroy();
        rechageWeb = null;
    }
}
