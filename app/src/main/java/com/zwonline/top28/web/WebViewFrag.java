package com.zwonline.top28.web;

/**
 * Created by b5book on 01/11/2017.
 */

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.VIBRATOR_SERVICE;


public class WebViewFrag extends Fragment {

    public static final String URL_TO_LOAD = "URL_TO_LOAD";

    private WebView mWebView;
    private Bundle mWebviewBundle;

    private ProgressBar mProgressBarCircle;
    private ProgressBar mProgressBarHorizontal;

    private RelativeLayout linearLayout;

    public String urlToLoad;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageA;

    public WebViewFrag(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(linearLayout == null){
            linearLayout = (RelativeLayout) inflater.inflate(R.layout.webview_layout, container, false);
            mWebView = (WebView) linearLayout.findViewById(R.id.fragWebView);
        }

        return linearLayout;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadWebPage();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadWebPage(){
        if (mWebviewBundle == null){

            mWebView.getSettings().setJavaScriptEnabled(true);

            mWebView.getSettings().setUserAgentString("app28/");

            mWebView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && event.getAction() == MotionEvent.ACTION_UP
                            && mWebView.canGoBack()) {
                        handler.sendEmptyMessage(1);
                        return true;
                    }
                    return false;
                }
            });

            mWebView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    Log.i("webview",""+newProgress);

                    mProgressBarCircle.setProgress(newProgress);
                    mProgressBarHorizontal.setProgress(newProgress);
                }

//                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                    if (mUploadMessage != null) {
//                        mUploadMessage.onReceiveValue(null);
//                    }
//                    mUploadMessage = uploadMsg;
//                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                    i.addCategory(Intent.CATEGORY_OPENABLE);
//                    String type = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
//                    i.setType(type);
//                }

                @Override
                @SuppressLint("NewApi")
                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                    if (mUploadMessageA != null) {
                        mUploadMessageA = null;
                    }
                    mUploadMessageA = filePathCallback;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null
                            && fileChooserParams.getAcceptTypes().length > 0) {
                        i.setType(fileChooserParams.getAcceptTypes()[0]);
                    } else {
                        i.setType("*/*");
                    }
                    ((ZWBaseActivity)getActivity()).showFileChooser(i);
                    return true;
                }

            });

            mWebView.setWebViewClient(new WebViewClient(){


                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

                    Log.i("webview","onPageStarted -- URL TO LOAD--" + urlToLoad);
                    Log.i("webview","onPageStarted -- URL NOW--" + url);
                    Log.i("webview","onPageStarted");
                }

                @Override
                public void onPageFinished(WebView view, String url) {

                    mProgressBarCircle.animate().alpha(0).setDuration(5000);
                    mProgressBarCircle.setVisibility(View.GONE);
                    mProgressBarHorizontal.animate().alpha(0).setDuration(500);
                    mProgressBarHorizontal.setVisibility(View.GONE);

                    super.onPageFinished(view, url);

                    webviewRunScripts();

                    AppCompatActivity activity = ((AppCompatActivity)getActivity());
                    if(activity != null){
                        activity.getSupportActionBar().setTitle(view.getTitle());
                    }

                    Log.i("webview", "onPageFinished ->" + url);
                }

//                @Override
//                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                    super.onReceivedError(view, errorCode, description, failingUrl);
//                    view.loadUrl("file:///android_asset/reload.html");
//                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                    Log.i("webview","override======");

                    //拦截微信支付
                    if (url.startsWith("weixin://wap/pay?")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));

                        PackageManager packageManager     = getActivity().getPackageManager();
                        List<ResolveInfo> resolvedActivities = packageManager.queryIntentActivities(intent, 0);
                        if(resolvedActivities.size() > 0){
                            startActivity(intent);
                        }else{
                            Toast.makeText(getActivity(),R.string.system_wx_noinstall, Toast.LENGTH_LONG);
                        }
                        return true;
                    }

                    if(url.contains("pushNewView")){
                        boolean showActBar = false;
                        if(url.contains("showNavBar")){
                            showActBar = true;
                        }

                        Intent intent = new Intent(getActivity(),WebViewActivity.class);
                        intent.putExtra(WebViewFrag.URL_TO_LOAD,url);//request.getUrl().toString());
                        intent.putExtra("showActBar",showActBar);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);

                        return true;
                    }

                    if(url.contains("top28app")) {
                        String commandStr = url.replace("http://top28app//", "");
                        String[] str = commandStr.split("/");

                        Log.i("webview", str[0]);

                        //显示输入框
                        if (str[0].equals("showInputBox")) {
                            ((ZWBaseActivity)getActivity()).showInputView();
                        }

                        //显示actionbar
                        if (str[0].equals("showNavBar")) {
                            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
                        }

                        //显示右上按钮
                        if (str[0].equals("showNavButton")) {

                        }

                        //显示分享
                        if (str[0].equals("showShareBox")) {
                            view.evaluateJavascript("top28appShareItem",new ValueCallback<String>() {
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

                                        UMImage imageToShare = new UMImage(getActivity(),icon);

                                        UMWeb web = new UMWeb(share_url);
                                        web.setTitle(title);//标题
                                        web.setThumb(imageToShare);  //缩略图
                                        web.setDescription(description);//描述

                                        UMShareListener umShareListener = new UMShareListener() {
                                            @Override
                                            public void onStart(SHARE_MEDIA share_media) {

                                            }

                                            @Override
                                            public void onResult(SHARE_MEDIA share_media) {
                                                view.evaluateJavascript("top28appShareStatus(true)",null);
                                            }

                                            @Override
                                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                Toast.makeText(getActivity(),throwable.toString(), Toast.LENGTH_SHORT).show();
                                                view.evaluateJavascript("top28appShareStatus(false)",null);
                                            }

                                            @Override
                                            public void onCancel(SHARE_MEDIA share_media) {
                                                view.evaluateJavascript("top28appShareStatus(false)",null);
                                            }
                                        };
                                        new ShareAction(getActivity())
                                                .withMedia(web)
                                                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                                                .setCallback(umShareListener)
                                                .open();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        //显示二维码
                        if (str[0].equals("showQRScan")) {
                            Log.i("webview", str[0]);
                            ((ZWBaseActivity)getActivity()).showQRScan();
                        }

                        //震动
                        if (str[0].equals("dildo")) {
                            Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
                            vibrator.vibrate(200);
                        }

                        //推出当前页
                        if (str[0].equals("popCurrentView")) {
                            if(!(getActivity() instanceof MainActivity)){
                                getActivity().finish();
                            }
                        }

                        //云信登陆
                        if (str[0].equals("imLogin")) {
                            view.evaluateJavascript("top28appIMCredentials",new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String value) {

                                    JSONObject json = null;
                                    String accid = null;
                                    String token = null;

                                    try {
                                        json = new JSONObject(value);

                                        accid = json.getString("accid");
                                        token = json.getString("passcode");

                                        if(!TextUtils.isEmpty(accid) && !TextUtils.isEmpty(token)){
                                            LoginInfo info = new LoginInfo(accid,token);
                                            SharedPreferences preferences = getActivity().getSharedPreferences("Top28Pref", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("NIMACCID", accid);
                                            editor.putString("NIMTOKEN", token);
                                            editor.apply();

                                            RequestCallback<LoginInfo> callback =
                                                    new RequestCallback<LoginInfo>() {
                                                        @Override
                                                        public void onSuccess(LoginInfo param) {
                                                            Log.i("NIMNIM","successss");
                                                            Toast.makeText(getActivity(),R.string.yx_suc_login, Toast.LENGTH_SHORT).show();;
                                                            view.evaluateJavascript("top28appIMLoginStatus('')",null);
                                                        }

                                                        @Override
                                                        public void onFailed(int code) {
                                                            Log.i("NIMNIM","fffffffff");
                                                            Toast.makeText(getActivity(),R.string.yx_fail_login + code, Toast.LENGTH_SHORT).show();;
                                                            view.evaluateJavascript("top28appIMLoginStatus('"+ code + "')",null);
                                                        }

                                                        @Override
                                                        public void onException(Throwable exception) {
                                                            Log.i("NIMNIM","eeeeeeeeee");
                                                            Toast.makeText(getActivity(),R.string.yx_fail_login + exception.getMessage() , Toast.LENGTH_SHORT).show();
                                                            view.evaluateJavascript("top28appIMLoginStatus('9999')",null);
                                                        }
                                                    };
                                            NIMClient.getService(AuthService.class)
                                                    .login(info)
                                                    .setCallback(callback);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        //云信登出
                        if (str[0].equals("imLogout")) {
                            NIMClient.getService(AuthService.class).logout();
                            SharedPreferences preferences = getActivity().getSharedPreferences("Top28Pref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("NIMACCID", "");
                            editor.putString("NIMTOKEN", "");
                            editor.commit();
                            Toast.makeText(getActivity(),R.string.yx_logout, Toast.LENGTH_SHORT).show();
                            mWebView.evaluateJavascript("top28appIMLogoutStatus('')", null);
                        }

                        //云信未读
//                        if (str[0].equals("imUnread")) {
//                            if(str.length > 1){
//                                String unreads = str[1];
//                                if(getActivity() instanceof MainActivity){
//                                    ((MainActivity)getActivity()).setNotificationForMessage(str[1]);
//                                }
//                                Log.i("NIMNIM","Unread===" + str[1]);
//                            }
//                        }

                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });

            mProgressBarCircle = (ProgressBar)getView().findViewById(R.id.fragProgressCircle);
            mProgressBarHorizontal = (ProgressBar)getView().findViewById(R.id.fragProgressBar);

            mProgressBarCircle.setVisibility(View.VISIBLE);
            mProgressBarHorizontal.setVisibility(View.VISIBLE);

            mWebView.loadUrl(urlToLoad,getWebViewHeader());
//            mWebView.loadDataWithBaseURL();
        }else {
//            mWebView.restoreState(mWebviewBundle);

            webviewRunScripts();

            mProgressBarCircle = (ProgressBar)getView().findViewById(R.id.fragProgressCircle);
            mProgressBarHorizontal = (ProgressBar)getView().findViewById(R.id.fragProgressBar);

            mProgressBarCircle.setVisibility(View.GONE);
            mProgressBarHorizontal.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebviewBundle = new Bundle();
        mWebView.saveState(mWebviewBundle);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1: {
                    webViewGoBack();
                }
                break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void webviewRunScripts(){
        String script2 = "document.getElementsByClassName(\"footer\")[0].style.display='none';";
        mWebView.evaluateJavascript(script2, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }

    public static Map<String,String> getWebViewHeader(){
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        HashMap<String ,String> result = new HashMap<>();
        result.put("User-Agent","app28/android-" + manufacturer + "-" + model);
        result.put("Cookies",getDefaultCookie());
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void qrScanResult(String result){
        mWebView.evaluateJavascript("top28appStringInput('" + result +  "')",null);
    }

    public void reloadDefaultURL(){
        mWebView.loadUrl(urlToLoad);
    }

    public static String getDefaultCookie(){
//        Context context = BaseApplication.getDefaultInstance();
//        String version = AppInfoUtil.getVersionToPhp(context);
//        String version_name = AppInfoUtil.getAppVersion(context);
//        String device = DeviceInfo.getDeviceName();
//        String packageName = AppInfoUtil.getPackageName(context);
//        String appKey = AppInfoUtil.getAppKey(context);
//        String channel = AppInfoUtil.getDefaultChannel(context);
//        String deviceId = DeviceInfo.generateDeviceId(context);
//        String netType = DeviceInfo.getNetworkTypeWIFI2G3G(context);
        String platform = "Android";
//        String resolution = DeviceInfo.getResolution(context);
//        String sysVer = DeviceInfo.getOsVersion();
//        String uid = CommonSharePreference.getUserId();
        String ts = System.currentTimeMillis()+"";
        StringBuilder sb = new StringBuilder();
//        sb.append("package="+packageName+";");
//        sb.append("appkey="+appKey+";");
//        sb.append("channel="+channel+";");
//        sb.append("version="+version+";");
//        sb.append("version_name="+version_name+";");
//        sb.append("deviceid="+deviceId+";");
//        sb.append("net-type="+netType+";");
        sb.append("platform="+platform+";");
//        sb.append("resolution="+resolution+";");
//        sb.append("sys_version="+sysVer+";");
        sb.append("ts="+ts+";");
//        sb.append("uid="+uid+";");
//        sb.append("devicename="+device+";");
        return sb.toString();
    }

    private void webViewGoBack(){
        mWebView.goBack();
    }

}
