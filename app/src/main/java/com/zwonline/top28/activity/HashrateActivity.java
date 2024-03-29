package com.zwonline.top28.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zwonline.top28.adapter.GuidePageAdapter;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.wxapi.RewritePopwindow;
import com.zwonline.top28.wxapi.ShareUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

/**
 * 鞅分挖矿
 */
public class HashrateActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private RelativeLayout back;
    private RelativeLayout backXx;
    private TextView hashrate;
    private TextView hashrates;
    private SharedPreferencesUtils sp;

    private ProgressBar progressBar;
    private WebView hashrateWeb;
    private String token;
    private String url = Api.baseUrl() + "/Integral/createIntegral?version=";
    private ImageView service;
    private RewritePopwindow mPopwindow;
    private ViewPager vp;
    private int[] imageIdArray;//图片资源的数组
    private List<View> viewList;//图片资源的集合
    private ViewGroup vg;//放置圆点
    //实例化原点View
    private ImageView iv_point;
    private ImageView[] ivPointArray;
    private SharedPreferences hashrateSp;
    private boolean isfristHashrate;
    private Button ib_start;
    private LinearLayout hashrate_linear;
    private RelativeLayout hashrate_guide;
    private boolean isLast = false;
    private int positions;
    private RelativeLayout netErro;
    private boolean isNetErro = true;

    @Override
    protected void init() {
        StatusBarUtil.setColor(this, Color.parseColor("#5023DC"), 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
        initView();
        hashrateSp = getSharedPreferences("startup", 0);
        //这个文件里面的布尔常量名，和它的初始状态，状态为是，则触发下面的方法
        isfristHashrate = hashrateSp.getBoolean("isfristHashrate", true);
        isFrist();
        ib_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLast) {
                    hashrate_linear.setVisibility(View.VISIBLE);
                    hashrate_guide.setVisibility(View.GONE);
                } else {
                    vp.setCurrentItem(positions + 1, true);
                }

            }
        });

        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        initViewPager();
        String cookieString = "PHPSESSID=" + token + "; path=/";
        synCookies(url + LanguageUitils.getVerName(this), cookieString);
        webSettingInit();
        //客服聊天
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NimUIKit.startP2PSession(HashrateActivity.this, "272");
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
    }

    /**
     * 判断是不是第一次下载app，如果是第一次下载显示引导页，不是直接显示转载文章页
     */
    private void isFrist() {
        if (isfristHashrate) {
            SharedPreferences.Editor edit = hashrateSp.edit();//创建状态储存文件
            edit.putBoolean("isfristHashrate", false);//将参数put，改变其状态
            edit.commit();//保证文件的创建和编辑
            hashrate_linear.setVisibility(View.GONE);
            hashrate_guide.setVisibility(View.VISIBLE);
            ib_start.setVisibility(View.VISIBLE);
        } else {
            hashrate_linear.setVisibility(View.VISIBLE);
            hashrate_guide.setVisibility(View.GONE);
            ib_start.setVisibility(View.GONE);
        }
    }

    //webview配置
    private void webSettingInit() {
        WebSettings settings = hashrateWeb.getSettings();
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
        hashrateWeb.loadUrl(url + LanguageUitils.getVerName(this), headMap);
        hashrateWeb.setWebViewClient(new WebViewClient() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
//                ToastUtil.showToast(getApplicationContext(),url);
                //算力详情
                if (url.contains("http://top28app/computePower/")) {
                    Intent intent1 = new Intent(HashrateActivity.this, IntegralActivity.class);
                    intent1.putExtra("type", BizConstant.NEW);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }
                //商机币
                if (url.contains("http://top28app/pushBoc/")) {
                    Intent intent1 = new Intent(HashrateActivity.this, IntegralActivity.class);
                    intent1.putExtra("type", BizConstant.RECOMMEND);
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
                    Intent intent1 = new Intent(HashrateActivity.this, EarnIntegralActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }
                //购买商机币
                if (url.contains("http://top28app/rechargeBusinessOpportunityCoin")) {
                    Intent intent1 = new Intent(HashrateActivity.this, IntegralPayActivity.class);
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
                    NimUIKit.startP2PSession(HashrateActivity.this, uids);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    return true;
                }

                // ------  对alipays:相关的scheme处理 -------
                if (url.startsWith("alipays:") || url.startsWith("alipay")) {
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    } catch (Exception e) {
                        new AlertDialog.Builder(HashrateActivity.this)
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
                //跳转文章详情
                if (url.contains("https://toutiao.28.com/Index/article/id")) {
//                    service.setVisibility(View.VISIBLE);
                    String path = "https://toutiao.28.com/Index/article/id/";
                    String paths = ".html";
                    String ids = url.substring(path.length(), url.length() - paths.length());
                    Intent intent = new Intent(HashrateActivity.this, HomeDetailsActivity.class);
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

                //判断用户单击的是那个超连接
                String tag = "tel";
                if (url.contains(tag)) {
                    String mobile = url.substring(url.lastIndexOf("/") + 1);
                    Intent mIntent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse(mobile);
                    mIntent.setData(data);
                    //Android6.0以后的动态获取打电话权限
                    if (ActivityCompat.checkSelfPermission(HashrateActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(mIntent);
                        //这个超连接,java已经处理了，webview不要处理
                        return true;
                    } else {
                        //申请权限
                        ActivityCompat.requestPermissions(HashrateActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        return true;
                    }
                }
                if (url.contains("top28app")) {
                    String commandStr = url.replace("http://top28app//", "");
                    String[] str = commandStr.split("/");


                    if (str[0].equals("showNavBar")) {
                        ((AppCompatActivity) HashrateActivity.this).getSupportActionBar().show();
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
                                    mPopwindow = new RewritePopwindow(HashrateActivity.this, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mPopwindow.dismiss();
                                            mPopwindow.backgroundAlpha(HashrateActivity.this, 1f);
                                            switch (v.getId()) {
                                                case R.id.weixinghaoyou:
                                                    ShareUtils.shareWeb(HashrateActivity.this, finalShare_url, finalTitle
                                                            , finalDescription, finalIcon, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                                                    );
                                                    break;
                                                case R.id.pengyouquan:
                                                    ShareUtils.shareWeb(HashrateActivity.this, finalShare_url, finalTitle
                                                            , finalDescription, finalIcon, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                                                    );
                                                    break;
                                                case R.id.qqhaoyou:
                                                    ShareUtils.shareWeb(HashrateActivity.this, finalShare_url, finalTitle
                                                            , finalDescription, finalIcon, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
                                                    );
                                                    break;
                                                case R.id.qqkongjian:
                                                    ShareUtils.shareWeb(HashrateActivity.this, finalShare_url, finalTitle
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
//
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
                if (hashrateWeb.canGoBack()) {
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
                AlertDialog.Builder b = new AlertDialog.Builder(HashrateActivity.this);
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
                AlertDialog.Builder b = new AlertDialog.Builder(HashrateActivity.this);
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

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_hashrate;
    }

    private void initView() {
        service = (ImageView) findViewById(R.id.service);
        back = (RelativeLayout) findViewById(R.id.back);
        backXx = (RelativeLayout) findViewById(R.id.back_xx);
        hashrate = (TextView) findViewById(R.id.hashrate);
        hashrates = (TextView) findViewById(R.id.hashrates);
        progressBar = (ProgressBar) findViewById(R.id.progress_Bar);
        hashrateWeb = (WebView) findViewById(R.id.hashrate_web);
        hashrate_guide = (RelativeLayout) findViewById(R.id.hashrate_guide);
        hashrate_linear = (LinearLayout) findViewById(R.id.hashrate_linear);
        ib_start = (Button) findViewById(R.id.guide_ib_start);
        netErro = (RelativeLayout) findViewById(R.id.net_erro);
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

    }

    //Dialog弹窗关注
    private void showNormalDialogs(final String invitationCode) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(HashrateActivity.this);
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

    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {
        vp = (ViewPager) findViewById(R.id.guide_vp);
        //实例化图片资源
        imageIdArray = BizConstant.INTEGRALDIG;
        viewList = new ArrayList<>();
        //获取一个Layout参数，设置为全屏
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        //循环创建View并加入到集合中
        int len = imageIdArray.length;
        for (int i = 0; i < len; i++) {
            //new ImageView并设置全屏和图片资源
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(imageIdArray[i]);

            //将ImageView加入到集合中
            viewList.add(imageView);
        }

        //View集合初始化好后，设置Adapter
        vp.setAdapter(new GuidePageAdapter(viewList));
        //设置滑动监听
        vp.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑动后的监听
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        this.positions = position;
        //循环设置当前页的标记图
        int length = imageIdArray.length;
//        for (int i = 0; i < length; i++) {
//            ivPointArray[position].setBackgroundResource(R.mipmap.guide1);
//            if (position != i) {
//                ivPointArray[i].setBackgroundResource(R.mipmap.guide1);
//            }
//        }

        //判断是否是最后一页，若是则显示按钮
        if (position == imageIdArray.length - 1) {
            ib_start.setText("朕已阅");
            ib_start.setTextColor(Color.parseColor("#FFFFFF"));
            ib_start.setBackgroundResource(R.drawable.btn_red_shape);
            isLast = true;
        } else {
            ib_start.setText("下一页");
            ib_start.setTextColor(Color.parseColor("#FF2B2B"));
            ib_start.setBackgroundResource(R.drawable.reword__shape);
            isLast = false;
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
