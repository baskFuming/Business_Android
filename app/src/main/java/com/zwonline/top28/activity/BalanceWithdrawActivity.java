package com.zwonline.top28.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.CookieSyncManager;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

/**
 * 描述：提现的页面
 *
 * @author YSG
 * @date 2018/3/14
 */
public class BalanceWithdrawActivity extends BaseActivity {

    private RelativeLayout back;
    private TextView tvBalance;
    private ProgressBar withdrawProgressBar;
    private WebView balanceWeb;
    private RelativeLayout backXX;
    private SharedPreferencesUtils sp;
    private String token;
    public String url = Api.baseUrl()+"/Members/money.html";

    @Override
    protected void init() {
        initView();
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        String cookieString = "PHPSESSID=" + token + "; path=/";
//        CookieManager.getInstance().setCookie(url, cookieString);
        synCookies(url,cookieString);

        WebSettings webSettings = balanceWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
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
        balanceWeb.loadUrl(url, headMap);
        balanceWeb.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    withdrawProgressBar.setVisibility(View.GONE);
                } else {
                    withdrawProgressBar.setVisibility(View.VISIBLE);
                    withdrawProgressBar.setProgress(newProgress);
                }
            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tvBalance.setText(title);
                if (balanceWeb.canGoBack()){
                    backXX.setVisibility(View.VISIBLE);
                }else {
                    backXX.setVisibility(View.GONE);

                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(BalanceWithdrawActivity.this);
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
                AlertDialog.Builder b = new AlertDialog.Builder(BalanceWithdrawActivity.this);
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

        balanceWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                ToastUtils.showToast(ArticleActivity.this,"url="+url);

                //添加银行卡
                if (url.contains(BizConstant.INTERCEPT_ADD_BANK)) {
                    Intent intent =new Intent(BalanceWithdrawActivity.this,AddBankActivity.class);
                    intent.putExtra("add_card","1");
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
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

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        withdrawProgressBar = (ProgressBar) findViewById(R.id.withdraw_progress_Bar);
        balanceWeb = (WebView) findViewById(R.id.balance_web);
        backXX= (RelativeLayout) findViewById(R.id.back_xx);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_balance_withdraw;
    }


    @OnClick({R.id.back, R.id.withdraw_record, R.id.back_xx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (balanceWeb.canGoBack()) {
                    balanceWeb.goBack();// 返回前一个页面
                } else {
                    finish();
                }
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.withdraw_record:
                startActivity(new Intent(BalanceWithdrawActivity.this,WithdrawRecordActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.back_xx:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && balanceWeb.canGoBack()) {
            balanceWeb.goBack();// 返回前一个页面
            backXX.setVisibility(View.VISIBLE);
            return true;
        }else {
            backXX.setVisibility(View.GONE);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        balanceWeb.stopLoading();
        balanceWeb.removeAllViews();
        balanceWeb.destroy();
        balanceWeb = null;
    }


    public static void synchronousWebCookies(Context context, String url, String cookies){
        if ( !StringUtil.isEmpty(url) )
            if (!StringUtil.isEmpty(cookies) ) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                    CookieSyncManager.createInstance( context);
                }
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie( true );
                cookieManager.removeSessionCookie();// 移除
                cookieManager.removeAllCookie();
                StringBuilder sbCookie = new StringBuilder();//创建一个拼接cookie的容器,为什么这么拼接，大家查阅一下http头Cookie的结构
                sbCookie.append(cookies);//拼接sessionId
                //			       sbCookie.append(String.format(";domain=%s", ""));
                //			       sbCookie.append(String.format(";path=%s", ""));
                String cookieValue = sbCookie.toString();
                cookieManager.setCookie(url, cookieValue);//为url设置cookie
                CookieSyncManager.getInstance().sync();//同步cookie
                String newCookie = cookieManager.getCookie(url);
                Log.i("同步后cookie", newCookie);
            }
    }

}
