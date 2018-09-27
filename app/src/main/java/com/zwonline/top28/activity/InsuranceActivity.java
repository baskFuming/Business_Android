package com.zwonline.top28.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import butterknife.OnClick;

/**
 * 描述：创业保障计划
 *
 * @author YSG
 * @date 2018/1/10
 */
public class  InsuranceActivity extends BaseActivity {

    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.back_xx)
    RelativeLayout backXx;
    @BindView(R.id.insurance_title)
    TextView insuranceTitle;
    @BindView(R.id.progress_Bar)
    ProgressBar progressBar;
    @BindView(R.id.insurance_sign)
    WebView insuranceSign;
    //        https://toutiao.28.com/Members/insurance.html
    private SharedPreferencesUtils sp;
    private String token;
    //    private String url = "https://toutiao.28.com/Members/insurance_agreement.html";
    private String url = Api.baseUrl() + "/Members/shouyuebao";

    @Override
    protected void init() {
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        WebSettings settings = insuranceSign.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);// 设置支持缩放
        settings.setJavaScriptEnabled(true);//支持JS
        insuranceSign.setWebViewClient(new WebViewClient());//禁止跳系统浏览器
        String cookieString = "PHPSESSID=" + token + "; path=/";
        synCookies(url, cookieString);
//        CookieManager.getInstance().setCookie(url, cookieString);
        settings.setUserAgentString("app28/");
        //请求头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Accept-Language", LanguageUitils.getCurCountryLan());
        insuranceSign.loadUrl(url, headMap);
        //设置响应js 的Alert()函数
        insuranceSign.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                insuranceTitle.setText(title);
                if (insuranceSign.canGoBack()) {
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
                AlertDialog.Builder b = new AlertDialog.Builder(InsuranceActivity.this);
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
                AlertDialog.Builder b = new AlertDialog.Builder(InsuranceActivity.this);
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


        insuranceSign.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //跳转到个人主页
                if (url.contains("http://top28app//popToUserCenter/")) {
                    finish();
                    overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                    return true;
                }

                return false;
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
        return R.layout.activity_insurance;
    }


    @OnClick({R.id.back, R.id.back_xx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (insuranceSign.canGoBack()) {
                    insuranceSign.goBack();// 返回前一个页面
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
        if ((keyCode == KeyEvent.KEYCODE_BACK) && insuranceSign.canGoBack()) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        insuranceSign.stopLoading();
        insuranceSign.removeAllViews();
        insuranceSign.destroy();
        insuranceSign = null;
    }


}
