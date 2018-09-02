package com.zwonline.top28.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

/**
 * 描述：考察项目
 *
 * @author YSG
 * @date 2018/2/4
 */
public class ProjectActivity extends BaseActivity {
    private RelativeLayout backXx;
    private TextView projectTeitle;
    private SharedPreferencesUtils sp;
    private String uid;
    private String token;
    private ProgressBar progressBar;
    private WebView projectWeb;
    private RelativeLayout back;

    @Override
    protected void init() {
        projectTeitle=(TextView)findViewById(R.id.project_title);
        progressBar = (ProgressBar) findViewById(R.id.progress_project);
        backXx=(RelativeLayout) findViewById(R.id.back_xx);
        projectWeb = (WebView) findViewById(R.id.project_web);
        back = (RelativeLayout) findViewById(R.id.project_back);
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        String cookieString = "PHPSESSID=" + token + "; path=/";
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        WebSettings settings = projectWeb.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);// 设置支持缩放
        settings.setJavaScriptEnabled(true);//支持JS
        projectWeb.setWebViewClient(new WebViewClient());//禁止跳系统浏览器
//        CookieManager.getInstance().setCookie("https://toutiao.28.com/Review/inspect/uid/" + uid + ".html", cookieString);
        synCookies(Api.baseUrl()+"/Review/inspect/uid/" + uid + ".html", cookieString);
        settings.setUserAgentString("app28/");
        //请求头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Accept-Language", LanguageUitils.getCurCountryLan());
        projectWeb.loadUrl(Api.baseUrl()+"/Review/research_details/id/" + uid + ".html", headMap);
        projectWeb.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                projectTeitle.setText(title);
                if (projectWeb.canGoBack()) {
                    backXx.setVisibility(View.VISIBLE);
                } else {
                    backXx.setVisibility(View.GONE);

                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(ProjectActivity.this);
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
                AlertDialog.Builder b = new AlertDialog.Builder(ProjectActivity.this);
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

        projectWeb.setWebViewClient(new WebViewClient() {

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
        return R.layout.activity_project;
    }


    @OnClick({R.id.project_back, R.id.back_xx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.project_back:
                if (projectWeb.canGoBack()) {
                    projectWeb.goBack();// 返回前一个页面
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
        finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
        return super.onKeyDown(keyCode, event);
    }
}
