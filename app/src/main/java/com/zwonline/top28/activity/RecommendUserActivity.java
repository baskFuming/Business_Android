package com.zwonline.top28.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.RecommendUserBean;
import com.zwonline.top28.presenter.ReconmmnedUserPresenter;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.popwindow.RecommendPopwindow;
import com.zwonline.top28.view.IRecommnedActivity;
import com.zwonline.top28.wxapi.RewritePopwindow;
import com.zwonline.top28.wxapi.ShareUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述:用户推荐
 *
 * @author YSG
 * @date 2018/1/9
 */
public class RecommendUserActivity extends BaseActivity<IRecommnedActivity, ReconmmnedUserPresenter> implements IRecommnedActivity {
    private String TAG = "RecommendUserActivity";
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.back_xx)
    RelativeLayout backXx;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.progress_Bar)
    ProgressBar progressBar;
    @BindView(R.id.recommend_user_web)
    WebView recommendUserWeb;
    @BindView(R.id.text_content)
    TextView textmContent;

    private SharedPreferencesUtils sp;
    private String uid;
    private String token;
    //    private String url = "http://toutiao.28.com/Members/recommend_list.html";
    private String url;
    private RewritePopwindow mPopwindow;

    private RecommendPopwindow recommendPopwindow;
    private List<RecommendUserBean.DataBean> list;
    private List<RecommendUserBean.DataBean.ShareDataBean> dlist;
    private String mContent;
    private String strUrl;
    private String saveUrl;
    private String shareTitle;
    private String shareIcon;
    private String shareUrl;
    private String shareDes;

    @Override
    protected void init() {
        presenter.myRecommned(this);
        list = new ArrayList<>();
        dlist = new ArrayList<>();
        sp = SharedPreferencesUtils.getUtil();
        url = getIntent().getStringExtra("jumPath");
        token = (String) sp.getKey(this, "dialog", "");
        String cookieString = "PHPSESSID=" + token + "; path=/";
        synCookies(url, cookieString);
        WebSettings settings = recommendUserWeb.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);// 设置支持缩放
        settings.setJavaScriptEnabled(true);//支持JS
        settings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
//        CookieManager.getInstance().setCookie(url, cookieString);
        settings.setUserAgentString("app28/");
        //请求头
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Accept-Language", LanguageUitils.getCurCountryLan());
        recommendUserWeb.loadUrl(url, headMap);

        recommendUserWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tvTitle.setText(title);
                if (recommendUserWeb.canGoBack()) {
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
                AlertDialog.Builder b = new AlertDialog.Builder(RecommendUserActivity.this);
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
                AlertDialog.Builder b = new AlertDialog.Builder(RecommendUserActivity.this);
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
        recommendUserWeb.setWebViewClient(new WebViewClient() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {

                if (url.contains("top28app")) {
                    String commandStr = url.replace("http://top28app//", "");
                    String[] str = commandStr.split("/");

                    Log.i("webview", str[0]);

                    if (str[0].equals("showNavBar")) {
                        ((AppCompatActivity) RecommendUserActivity.this).getSupportActionBar().show();
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

                                    UMImage imageToShare = new UMImage(RecommendUserActivity.this, icon);

                                    UMWeb web = new UMWeb(share_url);
                                    web.setTitle(title);//标题
                                    web.setThumb(imageToShare);  //缩略图
                                    web.setDescription(description);//描述
                                    final String finalShare_url = share_url;
                                    final String finalTitle = title;
                                    final String finalDescription = description;
                                    final String finalIcon = icon;
                                    mPopwindow = new RewritePopwindow(RecommendUserActivity.this, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mPopwindow.dismiss();
                                            mPopwindow.backgroundAlpha(RecommendUserActivity.this, 1f);
                                            switch (v.getId()) {
                                                case R.id.weixinghaoyou:
                                                    ShareUtils.shareWeb(RecommendUserActivity.this, finalShare_url, finalTitle
                                                            , finalDescription, finalIcon, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                                                    );
                                                    break;
                                                case R.id.pengyouquan:
                                                    ShareUtils.shareWeb(RecommendUserActivity.this, finalShare_url, finalTitle
                                                            , finalDescription, finalIcon, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                                                    );
                                                    break;
                                                case R.id.qqhaoyou:
                                                    ShareUtils.shareWeb(RecommendUserActivity.this, finalShare_url, finalTitle
                                                            , finalDescription, finalIcon, R.mipmap.ic_launcher, SHARE_MEDIA.QQ
                                                    );
                                                    break;
                                                case R.id.qqkongjian:
                                                    ShareUtils.shareWeb(RecommendUserActivity.this, finalShare_url, finalTitle
                                                            , finalDescription, finalIcon, R.mipmap.ic_launcher, SHARE_MEDIA.QZONE
                                                    );
                                                    break;
                                                case R.id.copyurl:
                                                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                                    // 将文本内容放到系统剪贴板里。
                                                    cm.setText(finalShare_url + "#" + finalTitle);
                                                    ToastUtils.showToast(RecommendUserActivity.this, "复制成功");
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    });
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
            }
        });
    }

    @Override
    protected ReconmmnedUserPresenter getPresenter() {
        return new ReconmmnedUserPresenter(this);
    }

    private View.OnClickListener listem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected int setLayoutId() {
        return R.layout.activity_recommend_user;
    }


    @OnClick({R.id.back, R.id.back_xx, R.id.share_recommned, R.id.redpacket_recommned, R.id.redpacket_record, R.id.recommned_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (recommendUserWeb.canGoBack()) {
                    recommendUserWeb.goBack();// 返回前一个页面
                } else {
                    finish();
                }
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.back_xx:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.share_recommned: //分享推荐
                recommendPopwindow = new RecommendPopwindow(RecommendUserActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recommendPopwindow.dismiss();
                        recommendPopwindow.backgroundAlpha(RecommendUserActivity.this, 1f);
                        switch (v.getId()) {
                            case R.id.weixinghaoyou:
                                ShareUtils.shareWeb(RecommendUserActivity.this, Api.baseUrl()+shareUrl, shareTitle
                                        , shareDes, shareIcon, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN
                                );
                                break;
                            case R.id.pengyouquan:
                                ShareUtils.shareWeb(RecommendUserActivity.this, Api.baseUrl()+shareUrl, shareTitle
                                        , shareDes, shareIcon, R.mipmap.ic_launcher, SHARE_MEDIA.WEIXIN_CIRCLE
                                );
                                break;
                            case R.id.qqkongjian:
                                Intent saveintent = new Intent(RecommendUserActivity.this, SaveRecommnedActivity.class);
                                saveintent.putExtra("saveUrl", saveUrl);
                                startActivity(saveintent);
                                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                                break;
                            case R.id.copyurl:
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                // 将文本内容放到系统剪贴板里。
                                cm.setText(Api.baseUrl() + shareUrl+"#"+shareTitle);
                                ToastUtils.showToast(RecommendUserActivity.this, "复制成功");
                                break;
                            default:
                                break;
                        }
                    }
                });
                recommendPopwindow.showAtLocation(view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.redpacket_recommned://红包推荐 鞅分红包  商机币红包
                ToastUtils.showToast(RecommendUserActivity.this, "红包推荐");
                break;
            case R.id.redpacket_record://红包记录
                Intent intentrecord = new Intent(this, RedPacketRecordActivity.class);
                startActivity(intentrecord);
                this.overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.recommned_list://推荐列表
                Intent intentlist = new Intent(this, RecommendListActivity.class);
                intentlist.putExtra("strUrl", strUrl);
                startActivity(intentlist);
                this.overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void successRecommed(RecommendUserBean recommendUserBean) {
        if (recommendUserBean.status == 1) {
            list.add(recommendUserBean.data);
            mContent = recommendUserBean.data.title_content;
            strUrl = recommendUserBean.data.recommend_list_url;
            saveUrl = recommendUserBean.data.recommend_url;
            shareTitle = recommendUserBean.data.share_data.share_title;
            shareIcon = recommendUserBean.data.share_data.share_icon;
            shareUrl = recommendUserBean.data.share_data.share_url;
            shareDes = recommendUserBean.data.share_data.share_description;
        }
        textmContent.setText(mContent);
    }

    @Override
    public void successRecommedList(List<RecommendUserBean.DataBean> recommendlist) {

    }

    @Override
    public void onErro() {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && recommendUserWeb.canGoBack()) {
            recommendUserWeb.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
