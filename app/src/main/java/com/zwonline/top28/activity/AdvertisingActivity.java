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
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;

import butterknife.OnClick;

/**
 * 描述：广告页webview
 *
 * @author YSG
 * @date 2018/4/5
 */
public class AdvertisingActivity extends BaseActivity {

    private RelativeLayout back;
    private RelativeLayout backXx;
    private TextView tvAdvertising;
    private ProgressBar advertisingProgressBar;
    private WebView advertisingWeb;
    private String url;

    @Override
    protected void init() {
        initView();
        Intent intent = getIntent();
        tvAdvertising.setText(intent.getStringExtra("title"));
        url = intent.getStringExtra("jump_path");
        initWeb();
    }
    private void initWeb() {

        WebSettings webSettings = advertisingWeb.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //可执行js
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; SAMSUNG-SM-N900A Build/tt) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36:app28/");
        // 使用localStorage则必须打开
        webSettings.setDomStorageEnabled(true);
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//支持js
        webSettings.setJavaScriptEnabled(true);
        advertisingWeb.loadUrl(url);
        advertisingWeb.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    advertisingProgressBar.setVisibility(View.GONE);
                } else {
                    advertisingProgressBar.setVisibility(View.VISIBLE);
                    advertisingProgressBar.setProgress(newProgress);
                }
            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                tvAdvertising.setText(title);
                if (advertisingWeb.canGoBack()) {
                    backXx.setVisibility(View.GONE);
                } else {
                    backXx.setVisibility(View.GONE);

                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(AdvertisingActivity.this);
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
                AlertDialog.Builder b = new AlertDialog.Builder(AdvertisingActivity.this);
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

        advertisingWeb.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
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
        return R.layout.activity_advertising;
    }

    @OnClick({R.id.back, R.id.back_xx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
//                if (advertisingWeb.canGoBack()) {
//                    advertisingWeb.goBack();// 返回前一个页面
//                } else {
//                    finish();
//                }
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;

            case R.id.back_xx:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && advertisingWeb.canGoBack()) {
            advertisingWeb.goBack();// 返回前一个页面
            backXx.setVisibility(View.VISIBLE);
            return true;
        } else {
            backXx.setVisibility(View.GONE);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        advertisingWeb.stopLoading();
        advertisingWeb.removeAllViews();
        advertisingWeb.destroy();
        advertisingWeb = null;
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        backXx = (RelativeLayout) findViewById(R.id.back_xx);
        tvAdvertising = (TextView) findViewById(R.id.tv_advertising);
        advertisingProgressBar = (ProgressBar) findViewById(R.id.advertising_progress_Bar);
        advertisingWeb = (WebView) findViewById(R.id.advertising_web);
    }

}
