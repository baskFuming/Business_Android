package com.zwonline.top28.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.netease.nim.uikit.api.NimUIKit;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.activity.ChannelActivity;
import com.zwonline.top28.web.BaseWebViewActivity;
import com.zwonline.top28.wxapi.RewritePopwindow;
import com.zwonline.top28.wxapi.ShareUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

/**
 * 更多频道管理
 */
public class ChannelActivity extends BaseActivity {

    private RelativeLayout back;
    private RelativeLayout backXx;
    private TextView hashrate;
    private TextView hashrates;
    private ImageView service;
    private ProgressBar progressBar;
    private WebView hashrateWeb;
    private SharedPreferencesUtils sp;
    private String url;
    private RewritePopwindow mPopwindow;
    private String token;
    private String titleBarColor;
    private RelativeLayout netErro;
    private boolean isNetErro = true;

    @Override
    protected void init() {
        Intent intent = getIntent();
        url = intent.getStringExtra("weburl");
        titleBarColor = getIntent().getStringExtra("titleBarColor");
        if (StringUtil.isNotEmpty(titleBarColor)) {
            StatusBarUtil.setColor(this, Color.parseColor(titleBarColor), 0);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
        }
        initView();

        String uid = getIntent().getStringExtra("uid");
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        String cookieString = "PHPSESSID=" + token + "; path=/";
        if (StringUtil.isNotEmpty(url))
            synCookies(url, cookieString);
        webSettingInit();
        //客服聊天
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NimUIKit.startP2PSession(ChannelActivity.this, "272");
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_base_web_view;
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        backXx = (RelativeLayout) findViewById(R.id.back_xx);
        hashrate = (TextView) findViewById(R.id.hashrate);
        hashrates = (TextView) findViewById(R.id.hashrates);
        service = (ImageView) findViewById(R.id.service);
        progressBar = (ProgressBar) findViewById(R.id.progress_Bar);
        hashrateWeb = (WebView) findViewById(R.id.hashrate_web);
        ImageView backImage = (ImageView) findViewById(R.id.back_image);
        ImageView backXImage = (ImageView) findViewById(R.id.backx_image);
        netErro = (RelativeLayout) findViewById(R.id.net_erro);
        RelativeLayout backgroud_relative = (RelativeLayout) findViewById(R.id.backgroud_relative);
        if (StringUtil.isNotEmpty(titleBarColor)) {
            backImage.setImageResource(R.mipmap.back);
            backXImage.setImageResource(R.mipmap.close_x);
            hashrate.setTextColor(Color.WHITE);
            hashrates.setTextColor(Color.WHITE);
            backgroud_relative.setBackgroundColor(Color.parseColor(titleBarColor));
        } else {
            backImage.setImageResource(R.mipmap.return_black);
            backXImage.setImageResource(R.mipmap.back_x);
            hashrate.setTextColor(Color.BLACK);
            hashrates.setTextColor(Color.BLACK);
            backgroud_relative.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    //webview配置
    private void webSettingInit() {
        WebSettings settings = hashrateWeb.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; SAMSUNG-SM-N900A Build/tt) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36:app28/");
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
        hashrateWeb.loadUrl(url, headMap);
        hashrateWeb.setWebViewClient(new WebViewClient() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
//                ToastUtil.showToast(getApplicationContext(),url);
                //跳转到个人主页
                if (url.contains("http://top28app/computePower/")) {
                    Intent intent1 = new Intent(ChannelActivity.this, IntegralActivity.class);
                    intent1.putExtra("type", BizConstant.TYPE_ONE);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }
                //点击下载App
                if (url.contains(".apk")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
                //赚取算力
                if (url.contains("http://top28app/computePowerTask/")) {
                    Intent intent1 = new Intent(ChannelActivity.this, EarnIntegralActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }
                //购买商机币
                if (url.contains("http://top28app/rechargeBusinessOpportunityCoin/")) {
                    Intent intent1 = new Intent(ChannelActivity.this, IntegralPayActivity.class);
                    intent1.putExtra("type", BizConstant.TYPE_TWO);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }
                //商机币
                if (url.contains("http://top28app/pushBoc/")) {
                    Intent intent1 = new Intent(ChannelActivity.this, IntegralActivity.class);
                    intent1.putExtra("type", BizConstant.RECOMMEND);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }
                if (url.contains("open28app")) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }

                //联系客服
                if (url.contains("http://top28app//pushToIM/")) {
                    String urls = url;
//                    service.setVisibility(View.VISIBLE);
                    String path = "http://top28app//pushToIM/";
                    String uids = url.substring(path.length(), url.length());
                    NimUIKit.startP2PSession(ChannelActivity.this, uids);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }

                // ------  对alipays:相关的scheme处理 -------
                if (url.startsWith("alipays:") || url.startsWith("alipay")) {
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    } catch (Exception e) {
                        new AlertDialog.Builder(ChannelActivity.this)
                                .setMessage("未检测到支付宝客户端，请安装后重试。")
                                .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri alipayUrl = Uri.parse("https://d.alipay.com");
                                        startActivity(new Intent("android.intent.action.VIEW", alipayUrl));
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                    return true;
                }

                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
//                if (url.startsWith("weixin://wap/pay?")) {
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                } else {
//                    Map<String, String> extraHeaders = new HashMap<String, String>();
//                    extraHeaders.put("Referer", "http://lebaopay.28.com");
//                    view.loadUrl(url, extraHeaders);
//                }
                //跳转文章详情
                if (url.contains("https://toutiao.28.com/Index/article/id")) {
//                    service.setVisibility(View.VISIBLE);
                    String path = "https://toutiao.28.com/Index/article/id/";
                    String paths = ".html";
                    String ids = url.substring(path.length(), url.length() - paths.length());
                    Intent intent = new Intent(ChannelActivity.this, HomeDetailsActivity.class);
                    intent.putExtra("id", ids);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }
                //复制邀请码
                if (url.contains("http://top28app//getMyInvitationCode/")) {
                    String path = "http://top28app//getMyInvitationCode/";
                    String invitationCode = url.substring(path.length(), url.length());
                    showNormalDialogs(invitationCode);
                    return true;
                }
                /**
                 * close等于1关闭页面  其他不关闭
                 * action等于1返回首页等于2跳转新的界面等于3跳转IM
                 *tagid返回首页的下标
                 * openurl跳转链接
                 */
                if (url.contains("GetJson")) {
                    String path = "https://app28/GetJson/";
                    String ids = null;
                    try {
                        ids = URLDecoder.decode(url.substring(path.length(), url.length()), "utf-8");
                        JSONObject jobj = new JSONObject(ids.toString());
                        String openurl = jobj.getString("openurl");
                        String tagid = jobj.getString("tagid");
                        String close = jobj.getString("close");
                        String action = jobj.getString("action");
                        if (StringUtil.isNotEmpty(close) && close.equals(BizConstant.IS_SUC)) {
                            if (StringUtil.isNotEmpty(action) && action.equals(BizConstant.TYPE_ONE)) {
                                MessageFollow messageFollow = new MessageFollow();
                                if (StringUtil.isNotEmpty(tagid)) {
                                    messageFollow.homeTag = Integer.parseInt(tagid);
                                    EventBus.getDefault().post(messageFollow);
                                }
                                finish();
                                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                            } else if (StringUtil.isNotEmpty(action) && action.equals(BizConstant.TYPE_TWO)) {
                                Intent intent = new Intent(ChannelActivity.this, BaseWebViewActivity.class);
                                intent.putExtra("weburl", openurl);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                            } else if (StringUtil.isNotEmpty(action) && action.equals(BizConstant.TYPE_THREE)) {
                                NimUIKit.startP2PSession(ChannelActivity.this, tagid);
                                finish();
                                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                            }
                        } else {
                            if (StringUtil.isNotEmpty(action) && action.equals(BizConstant.TYPE_ONE)) {
                                MessageFollow messageFollow = new MessageFollow();
                                if (StringUtil.isNotEmpty(tagid)) {
                                    messageFollow.homeTag = Integer.parseInt(tagid);
                                    EventBus.getDefault().post(messageFollow);
                                }
                                startActivity(new Intent(ChannelActivity.this, MainActivity.class));
                                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                            } else if (StringUtil.isNotEmpty(action) && action.equals(BizConstant.TYPE_TWO)) {
                                Intent intent = new Intent(ChannelActivity.this, BaseWebViewActivity.class);
                                intent.putExtra("weburl", openurl);
                                startActivity(intent);
                                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                            } else if (StringUtil.isNotEmpty(action) && action.equals(BizConstant.TYPE_THREE)) {
                                NimUIKit.startP2PSession(ChannelActivity.this, tagid);
                                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    Intent intent = new Intent(ChannelActivity.this, HomeDetailsActivity.class);
//                    intent.putExtra("id", ids);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
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
                    if (ActivityCompat.checkSelfPermission(ChannelActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(mIntent);
                        //这个超连接,java已经处理了，webview不要处理
                        return true;
                    } else {
                        //申请权限
                        ActivityCompat.requestPermissions(ChannelActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        return true;
                    }
                }
                if (url.contains("top28app")) {
                    String commandStr = url.replace("http://top28app//", "");
                    String[] str = commandStr.split("/");

                    Log.i("webview", str[0]);

                    if (str[0].equals("showNavBar")) {
                        ((AppCompatActivity) ChannelActivity.this).getSupportActionBar().show();
                    }

                    //显示右上按钮
                    if (str[0].equals("showNavButton")) {

                    }

                    //显示分享
                    if (str[0].equals("showShareBox")) {
                        view.evaluateJavascript("top28appShareItem", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                JSONObject json = null;
                                String description = null;
                                String icon = null;
                                String share_url = null;
                                String title = null;

                                try {
                                    json = new JSONObject(value);
                                    description = json.getString("description");
                                    icon = json.getString("icon");
                                    share_url = json.getString("share_url");
                                    title = json.getString("title");

                                    UMImage imageToShare = new UMImage(getApplicationContext(), icon);

                                    UMWeb web = new UMWeb(share_url);
                                    web.setTitle(title);//标题
                                    web.setThumb(imageToShare);  //缩略图
                                    web.setDescription(description);//描述
                                    final String finalShare_url = share_url;
                                    final String finalTitle = title;
                                    final String finalDescription = description;
                                    final String finalIcon = icon;
                                    mPopwindow = new RewritePopwindow(ChannelActivity.this, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mPopwindow.dismiss();
                                            mPopwindow.backgroundAlpha(ChannelActivity.this, 1f);
                                            switch (v.getId()) {
                                                case R.id.weixinghaoyou:
                                                    ShareUtils.shareWeb(ChannelActivity.this, finalShare_url, finalTitle
                                                            , finalDescription, finalIcon, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                                                    );
                                                    break;
                                                case R.id.pengyouquan:
                                                    ShareUtils.shareWeb(ChannelActivity.this, finalShare_url, finalTitle
                                                            , finalDescription, finalIcon, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                                                    );
                                                    break;
                                                case R.id.qqhaoyou:
                                                    ShareUtils.shareWeb(ChannelActivity.this, finalShare_url, finalTitle
                                                            , finalDescription, finalIcon, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
                                                    );
                                                    break;
                                                case R.id.qqkongjian:
                                                    ShareUtils.shareWeb(ChannelActivity.this, finalShare_url, finalTitle
                                                            , finalDescription, finalIcon, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
                                                    );
                                                    break;
                                                case R.id.copyurl:
                                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                                    // 将文本内容放到系统剪贴板里。
                                                    cm.setText(finalShare_url + "#" + finalTitle);
                                                    ToastUtils.showToast(getApplicationContext(), "复制成功");
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    },true);
                                    mPopwindow.showAtLocation(view,
                                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
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
                if (isNetErro) {
                    hashrateWeb.setVisibility(View.VISIBLE);
                    netErro.setVisibility(View.GONE);
                } else {
                    hashrateWeb.setVisibility(View.GONE);
                    netErro.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //Log.e(TAG, "onReceivedError: ----url:" + error.getDescription());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return;
                }
                // 在这里显示自定义错误页
                netErro.setVisibility(View.VISIBLE);
                hashrateWeb.setVisibility(View.GONE);
                hashrate.setVisibility(View.GONE);
                hashrates.setVisibility(View.GONE);
//                mProgressBar.setVisibility(View.GONE);
                isNetErro = false;
            }

            // 新版本，只会在Android6及以上调用
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (request.isForMainFrame()) { // 或者： if(request.getUrl().toString() .equals(getUrl()))
                    // 在这里显示自定义错误页
                    netErro.setVisibility(View.VISIBLE);
                    hashrateWeb.setVisibility(View.GONE);
                    hashrate.setVisibility(View.GONE);
                    hashrates.setVisibility(View.GONE);
                    isNetErro = false;
                }
            }
        });
        hashrateWeb.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (isNetErro) {
                    if (title.length() > 14) {
                        hashrate.setText(title);
                        hashrate.setVisibility(View.VISIBLE);
                        hashrates.setVisibility(View.GONE);
                    } else {
                        hashrates.setText(title);
                        hashrates.setVisibility(View.VISIBLE);
                        hashrate.setVisibility(View.GONE);
                    }
                } else {
                    hashrates.setVisibility(View.GONE);
                    hashrate.setVisibility(View.GONE);
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
                AlertDialog.Builder b = new AlertDialog.Builder(ChannelActivity.this);
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
                AlertDialog.Builder b = new AlertDialog.Builder(ChannelActivity.this);
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

    @OnClick({R.id.back, R.id.back_xx, R.id.retry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (hashrateWeb.canGoBack()) {
                    hashrateWeb.goBack();// 返回前一个页面
                    service.setVisibility(View.GONE);
                } else {
                    finish();
                }
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.back_xx:
                service.setVisibility(View.GONE);
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.retry:
                webSettingInit();
                isNetErro = true;
                break;
            default:
                break;
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && hashrateWeb.canGoBack()) {
            hashrateWeb.goBack();// 返回前一个页面
            backXx.setVisibility(View.VISIBLE);
            service.setVisibility(View.GONE);
            return true;
        } else {
            backXx.setVisibility(View.GONE);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hashrateWeb.stopLoading();
        hashrateWeb.removeAllViews();
        hashrateWeb.destroy();
        hashrateWeb = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }

    //Dialog弹窗关注
    private void showNormalDialogs(final String invitationCode) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ChannelActivity.this);
        normalDialog.setMessage("复制成功");

        normalDialog.setNegativeButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setText(invitationCode);
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
