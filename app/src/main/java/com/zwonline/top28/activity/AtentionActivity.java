package com.zwonline.top28.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.jaeger.library.StatusBarUtil;
import com.netease.nim.uikit.api.NimUIKit;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.utils.SharedPreferencesUtils;

/**
 * 描述：我的关注
 * @author YSG
 * @date 2018/2/6
 */
public class AtentionActivity extends AppCompatActivity {

    private RelativeLayout back;
    private TextView fans;
    private ProgressBar progressBar;
    private WebView attention_web;
    private SharedPreferencesUtils sp;
    private String token;
    private static String cookieString;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atention);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.reded),0);
        initView();
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        fans = (TextView) findViewById(R.id.fans);
        progressBar = (ProgressBar) findViewById(R.id.progress_Bar);
        attention_web = (WebView) findViewById(R.id.attention_web);
        final Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        cookieString = "PHPSESSID=" + token + "; path=/";
        synCookies(Api.baseUrl()+"/Members/user_follow/uid/" + uid + ".html", cookieString);
//        CookieManager.getInstance().setCookie(, cookieString);
        WebSettings settings = attention_web.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString("app28/");
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);//设置适应Html5的一些方法
        attention_web.loadUrl(Api.baseUrl()+"/Members/user_follow/uid/" + uid + ".html");
        attention_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //跳转到个人主页
                if (url.contains("http://top28app//pushToUserHomepage/")) {
                    String path = "http://top28app//pushToUserHomepage/";
                    String uid = url.substring(path.length(), url.length());
                    Intent intent1 = new Intent(AtentionActivity.this, HomePageActivity.class);
                    intent1.putExtra("uid", uid);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }
                //聊天
                if (url.contains("http://top28app//pushToIM/")) {
                    String path = "http://top28app//pushToIM/";
                    String uids = url.substring(path.length(), url.length());
                    NimUIKit.startP2PSession(AtentionActivity.this, uids);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }
                //判断用户单击的是那个超连接
                String tag = "tel";
                if (url.contains(tag)) {
                    String mobile = url.substring(url.lastIndexOf("/") + 1);
                    Intent mIntent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse(mobile);
                    mIntent.setData(data);
                    //Android6.0以后的动态获取打电话权限
                    if (ActivityCompat.checkSelfPermission(AtentionActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(mIntent);
                        //这个超连接,java已经处理了，webview不要处理
                        return true;
                    } else {
                        //申请权限
                        ActivityCompat.requestPermissions(AtentionActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        return true;
                    }
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
        attention_web.setWebChromeClient(new WebChromeClient() {
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
                AlertDialog.Builder b = new AlertDialog.Builder(AtentionActivity.this);
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
                AlertDialog.Builder b = new AlertDialog.Builder(AtentionActivity.this);
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
}
