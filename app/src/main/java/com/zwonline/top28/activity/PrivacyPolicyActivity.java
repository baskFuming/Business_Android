package com.zwonline.top28.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.zwonline.top28.R;
import com.zwonline.top28.utils.click.AntiShake;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
////webView.loadUrl("file:///android_asset/web/index.html");
public class PrivacyPolicyActivity extends AppCompatActivity {
    @BindView(R.id.user_pro_weView)
    WebView webViewUser;
    @BindView(R.id.progress_Bar)
    ProgressBar progressBar;
    private String UserPro = "https://toutiao.28.com/app/privacy.html";
    @BindView(R.id.title)
    TextView te_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        ButterKnife.bind(this);
        // 设置WebView的客户端
        webViewUser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;// 返回false
            }
        });
        WebSettings webSettings = webViewUser.getSettings();
        // 让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存
        webSettings.setAppCacheEnabled(true);
        // 将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        //加载静态网页链接
        webViewUser.loadUrl(UserPro);
        webViewUser.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.length()>0){
                    if (title.length() > 0) {
                        te_title.setText(title);
                        te_title.setVisibility(View.VISIBLE);
                    } else {
                        te_title.setText(title);
                        te_title.setVisibility(View.VISIBLE);
                    }
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
        });
    }
    @OnClick({R.id.back})
    public void onCliceListen(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }
}
