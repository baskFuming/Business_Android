package com.zwonline.top28.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.zwonline.top28.R;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.forward.androids.base.BaseActivity;

/**
 *  保存相册
 */
public class SaveRecommnedActivity extends BaseActivity {
    private String TAG = "RecommnedUsersActivity";
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_WRITE = 100;
    @BindView(R.id.web_recommed)
    WebView webViewRecommed;
    @BindView(R.id.progress_Bar)
    ProgressBar progressBar;
    @BindView(R.id.title)
    TextView te_title;
    @BindView(R.id.saveImag)
    LinearLayout lin_SaveImg;
    @BindView(R.id.save_liner)
    LinearLayout save_liner;
    private String token;
    private SharedPreferencesUtils sp;
    //http://toutiao.28.com/Members/myRecommendUserList.html
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommned_user);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        ButterKnife.bind(this);
        sp = SharedPreferencesUtils.getUtil();
        url = getIntent().getStringExtra("saveUrl");
        token = (String) sp.getKey(this, "dialog", "");
        String cookieString = "PHPSESSID=" + token + "; path=/";
        synCookies(url, cookieString);
        webSettingInit();
        lin_SaveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });
    }

    //WebView 方法配置
    private void webSettingInit() {
        WebSettings settings = webViewRecommed.getSettings();
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
        webViewRecommed.loadUrl(url, headMap);
        webViewRecommed.setWebViewClient(new WebViewClient() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
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
        webViewRecommed.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title.length() > 0) {
                    te_title.setText(title);
                    te_title.setVisibility(View.VISIBLE);
                } else {
                    te_title.setText(title);
                    te_title.setVisibility(View.VISIBLE);
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
    @OnClick({R.id.back, R.id.saveImag})
    public void onCliceListen(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.saveImag:
                //保存图片到相册
                ToastUtils.showToast(this, "保存图片到相册");
                break;
        }
    }

    //获取保存WebView的屏幕
    public Bitmap getWebViewBitmap(LinearLayout webViewRecommed) {
        int h = 0;
        Bitmap bitmap;
        // 获取webview实际高度
        for (int i = 0; i < webViewRecommed.getChildCount(); i++) {
            h += webViewRecommed.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(webViewRecommed.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        webViewRecommed.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        String strPath = getSaveImgSDPath();
        String strFileName = System.currentTimeMillis() + ".png";
        String newPath = strPath + strFileName;
        try {
            out = new FileOutputStream(newPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + newPath)));
                MediaScannerConnection.scanFile(
                        this,
                        new String[]{newPath},
                        new String[]{"image/jpeg"},
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });

                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
        return bitmap;
    }

    /**
     * 获得保存图片到本地的路径
     */
    public static String getSaveImgSDPath() {
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            return Environment.getExternalStorageDirectory().toString() + "/top28download";
        } else
            return "top28download";
    }

    private void saveImage() {
        Bitmap scrollViewBitmap = getWebViewBitmap(save_liner);
        if (scrollViewBitmap != null) {
            ToastUtil.showToast(this, "图片保存成功");
        }
    }
    //判断读写权限   保存图片监听方法
    public void onSavePrivateKeyClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "no write permission");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("External storage permission is necessary");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SaveRecommnedActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_EXTERNAL_WRITE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions(SaveRecommnedActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_EXTERNAL_WRITE);
            }
            return;
        } else {
            saveImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_EXTERNAL_WRITE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission to write external storage granted.");
                saveImage();
            }
        }
    }

    /**
     * 将cookie同步到WebView
     *
     * @param url          WebView要加载的url
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
