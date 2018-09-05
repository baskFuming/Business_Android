package com.zwonline.top28.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.api.NimUIKit;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.MyFansAdapter;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.MyFansBean;
import com.zwonline.top28.presenter.MyFansPresenter;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IMyFansActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

/**
 * 描述：我的粉丝
 *
 * @author YSG
 * @date 2018/2/6
 */
public class FansActivity extends BaseActivity<IMyFansActivity, MyFansPresenter> implements IMyFansActivity {


    private MyFansAdapter adapter;
    private String uid;
    private SharedPreferencesUtils sp;
    private String token;
    private static String cookieString;
    private RelativeLayout back;
    private RelativeLayout backXx;
    private RecyclerView myfansRecy;
    private TextView fans;
    private TextView no;
    private ProgressBar progressBar;
    private WebView fansWeb;

    @Override
    protected void init() {
        initData();
        final Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        fans.setText(intent.getStringExtra("fans"));
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        cookieString = "PHPSESSID=" + token + "; path=/";
//        CookieManager.getInstance().setCookie("https://toutiao.28.com/Members/user_fans/" + uid + ".html", cookieString);
        synCookies(Api.baseUrl() + "/Members/user_fans/" + uid + ".html", cookieString);
        WebSettings settings = fansWeb.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString("app28/");
        settings.setSupportZoom(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //设置缓存
        settings.setDomStorageEnabled(true);//设置适应Html5的一些方法
        //请求头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Accept-Language", LanguageUitils.getCurCountryLan());
        fansWeb.loadUrl(Api.baseUrl() + "/Members/user_fans/" + uid + ".html", headMap);
        fansWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //跳转到个人主页
                if (url.contains("http://top28app//pushToUserHomepage/")) {
                    String path = "http://top28app//pushToUserHomepage/";
                    String uid = url.substring(path.length(), url.length());
                    Intent intent1 = new Intent(FansActivity.this, HomePageActivity.class);
                    intent1.putExtra("uid", uid);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }
                //聊天
                if (url.contains("http://top28app//pushToIM/")) {
                    String path = "http://top28app//pushToIM/";
                    String uids = url.substring(path.length(), url.length());
                    NimUIKit.startP2PSession(FansActivity.this, uids);
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
                    if (ActivityCompat.checkSelfPermission(FansActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(mIntent);
                        //这个超连接,java已经处理了，webview不要处理
                        return true;
                    } else {
                        //申请权限
                        ActivityCompat.requestPermissions(FansActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
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
        fansWeb.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                fans.setText(title);
                if (fansWeb.canGoBack()) {
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
                AlertDialog.Builder b = new AlertDialog.Builder(FansActivity.this);
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
                AlertDialog.Builder b = new AlertDialog.Builder(FansActivity.this);
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
//        presenter.mMyFans(this, uid);
    }

    private void initData() {
        back = (RelativeLayout) findViewById(R.id.back);
        backXx = (RelativeLayout) findViewById(R.id.back_xx);
        myfansRecy = (RecyclerView) findViewById(R.id.myfans_recy);
        fans = (TextView) findViewById(R.id.fans);
        no = (TextView) findViewById(R.id.no);
        progressBar = (ProgressBar) findViewById(R.id.progress_Bar);
        fansWeb = (WebView) findViewById(R.id.fans_web);
    }

    @Override
    protected MyFansPresenter getPresenter() {
        return new MyFansPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_fans;
    }


    @Override
    public void showMyFansDate(final List<MyFansBean.DataBean> fansList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myfansRecy.setLayoutManager(linearLayoutManager);
        adapter = new MyFansAdapter(fansList, FansActivity.this);
        myfansRecy.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new MyFansAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                Intent intent = new Intent(FansActivity.this, HomePageActivity.class);
                intent.putExtra("nickname", fansList.get(position).nickname);
                intent.putExtra("avatars", fansList.get(position).avatars);
                intent.putExtra("uid", fansList.get(position).uid);
                intent.putExtra("is_attention", fansList.get(position).did_i_follow);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
    }

    @Override
    public void showMyFans(boolean flag) {
//        if (flag) {
//            no.setVisibility(View.GONE);
//            myfansRecy.setVisibility(View.VISIBLE);
//        } else {
//            no.setVisibility(View.VISIBLE);
//            myfansRecy.setVisibility(View.GONE);
//        }
    }

    @Override
    public void noLoadMore() {

    }

    @Override
    public void showAttention(AttentionBean attentionBean) {

    }

    @Override
    public void showUnAttention(AttentionBean attentionBean) {

    }


    @OnClick({R.id.back, R.id.back_xx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (fansWeb.canGoBack()) {
                    fansWeb.goBack();// 返回前一个页面
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

}
