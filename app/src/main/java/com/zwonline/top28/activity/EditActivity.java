package com.zwonline.top28.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.zwonline.top28.R;
import com.zwonline.top28.utils.Utils;
import com.zwonline.top28.utils.WeakDataHolder;
import com.zwonline.top28.utils.click.AntiShake;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 描述：编辑文章
 * @author YSG
 * @date 2018/1/12
 */
public class EditActivity extends AppCompatActivity {

    private  RelativeLayout  back;
    private  TextView  fans;
    private  ProgressBar  progressBar;
    private  WebView  editWeb;
    private  Button  compile;
    private  String  url;
    private  String  html;
    private  Unbinder  bind;

    @RequiresApi(api  =  Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected  void  onCreate(Bundle  savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        bind  =  ButterKnife.bind(this);
        initView();
        Intent  intent  =  getIntent();
        url  =  intent.getStringExtra("url");
        StatusBarUtil.setColor(this,  getResources().getColor(R.color.black),  0);
        WebSettings  webSettings  =  editWeb.getSettings();
        if  (Build.VERSION.SDK_INT  >=  Build.VERSION_CODES.LOLLIPOP)  {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //可执行js
        webSettings.setUserAgentString("Mozilla/5.0  (Linux;  Android  4.4.4;  SAMSUNG-SM-N900A  Build/tt)  AppleWebKit/537.36  (KHTML,  like  Gecko)  Version/4.0  Chrome/33.0.0.0  Mobile  Safari/537.36:app28/");
        //  使用localStorage则必须打开
        webSettings.setDomStorageEnabled(true);
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //支持js
        webSettings.setJavaScriptEnabled(true);
        editWeb.loadUrl(url);
        editWeb.setWebViewClient(new  WebViewClient()  {
            @Override
            public  void  onPageStarted(WebView  view,  String  url,  Bitmap  favicon)  {
                super.onPageStarted(view,  url,  favicon);
                compile.setEnabled(false);
                compile.setBackgroundResource(R.drawable.btn_shape);
            }

            @Override
            public  void  onPageFinished(WebView  view,  String  url)  {
                super.onPageFinished(view,  url);
                compile.setEnabled(true);
                compile.setBackgroundResource(R.drawable.btn_register_shape);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        editWeb.setWebChromeClient(new WebChromeClient() {
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick({R.id.back, R.id.compile})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                Utils.switchToLaunch(this);
                break;
            case R.id.compile:
                //获取webview的内容
                editWeb.evaluateJavascript("document.documentElement.outerHTML.toString()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.i("testlog", "value=" + value);
                        System.out.println("ValueSize = "+value.length());
                        Intent intent = new Intent(EditActivity.this, EditArticleActivity.class);
                        WeakDataHolder.getInstance().setData("html",value);
                        WeakDataHolder.getInstance().setData("url",url);
//                        intent.putExtra("html", Base64.encodeToString(value.getBytes(),Base64.DEFAULT));
//                        System.out.println("Base6"+Base64.encodeToString(value.getBytes(),Base64.DEFAULT));
//                        intent.putExtra("url", url);
//                        System.out.println("ValueUrl = "+url.length());
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    }
                });
                break;
        }
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        fans = (TextView) findViewById(R.id.fans);
        progressBar = (ProgressBar) findViewById(R.id.progress_Bar);
        editWeb = (WebView) findViewById(R.id.edit_web);
        compile = (Button) findViewById(R.id.compile);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editWeb.stopLoading();
        editWeb.removeAllViews();
        editWeb.destroy();
        editWeb = null;
        bind.unbind();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent myIntent;
            myIntent = new Intent(EditActivity.this, TransmitActivity.class);
            startActivity(myIntent);
            this.finish();
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String URL = (String) WeakDataHolder.getInstance().getData("url");
        editWeb.loadUrl(URL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        String URLs = (String) WeakDataHolder.getInstance().getData("url");
        editWeb.loadUrl(URLs);
    }
}
