package com.zwonline.top28.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.click.AntiShake;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

/**
 * 描述：银行卡支付
 *
 * @author YSG
 * @date 2018/3/16
 */
public class BankPayActivity extends BaseActivity {

    private RelativeLayout bankPayBack;
    private TextView bankPaytitle;
    private ProgressBar bankProgressBar;
    private WebView bankPayWebView;
    private RelativeLayout backPayXx;
    private SharedPreferencesUtils sp;
    private String token;
    public String url = Api.baseUrl()+"/Members/bank_card_datails/?order_id=";
    private String orderId;

    @Override
    protected void init() {
        initView();  //初始化控件
        sp = SharedPreferencesUtils.getUtil();
        Intent intent=getIntent();
        orderId = intent.getStringExtra("orderCode");
        token = (String) sp.getKey(this, "dialog", "");
        String cookieString = "PHPSESSID=" + token + "; path=/";
        synCookies(Api.baseUrl()+"/Members/bank_card_datails/?order_id=" + orderId, cookieString);
        webViewInit();
    }

    private void webViewInit() {
        WebSettings webSettings = bankPayWebView.getSettings();
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
        bankPayWebView.loadUrl(url + orderId, headMap);
        bankPayWebView.setWebChromeClient(new WebChromeClient() {
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
                bankPaytitle.setText(title);
                if (bankPayWebView.canGoBack()){
                    backPayXx.setVisibility(View.VISIBLE);
                }else {
                    backPayXx.setVisibility(View.GONE);

                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(BankPayActivity.this);
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
                AlertDialog.Builder b = new AlertDialog.Builder(BankPayActivity.this);
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

        bankPayWebView.setWebViewClient(new WebViewClient() {
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
        return R.layout.activity_bnak_pay;
    }

    private void initView() {
        bankPayBack = (RelativeLayout) findViewById(R.id.bank_pay_back);
        bankPaytitle = (TextView) findViewById(R.id.bank_paytitle);
        bankProgressBar = (ProgressBar) findViewById(R.id.bank_progress_Bar);
        bankPayWebView = (WebView) findViewById(R.id.bank_pay_web);
        backPayXx= (RelativeLayout) findViewById(R.id.back_pay_xx);
    }

//    @OnClick({R.id.bank_pay_back, R.id.withdraw_record})
    @OnClick({R.id.bank_pay_back, R.id.back_pay_xx})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.bank_pay_back:
                if (bankPayWebView.canGoBack()) {
                    bankPayWebView.goBack();// 返回前一个页面
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
        if ((keyCode == KeyEvent.KEYCODE_BACK) && bankPayWebView.canGoBack()) {
            bankPayWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bankPayWebView.stopLoading();
        bankPayWebView.removeAllViews();
        bankPayWebView.destroy();
        bankPayWebView = null;
    }
}
