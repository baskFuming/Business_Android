package com.zwonline.top28.web;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;



import java.io.File;


/**
 * Created by b5book on 24/11/2017.
 */

public class ZWBaseActivity extends AppCompatActivity {
    public WebViewFrag mWebViewFrag;

    public void showQRScan(){
        startActivityForResult(new Intent(this, TestScanActivity.class), 999);
    }

    public void showInputView(){
        startActivityForResult(new Intent(this, InputActivity.class), 999);
    }

    public void showFileChooser(Intent i){
        startActivityForResult(Intent.createChooser(i, "File Chooser"), 666);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("123","" + requestCode);
        if(requestCode == 999){
            if(resultCode == 123){
                mWebViewFrag.qrScanResult(data.getStringExtra("result"));
            }
        }

        if (requestCode == 666) {
            ValueCallback mUploadMessage = mWebViewFrag.mUploadMessageA;
            if (null == mUploadMessage) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result == null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
                return;
            }
//            CLog.i("UPFILE", "onActivityResult" + result.toString());
            String path =  FileUtils.getPath(this, result);
            if (TextUtils.isEmpty(path)) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
                return;
            }
            Uri uri = Uri.fromFile(new File(path));
//            CLog.i("UPFILE", "onActivityResult after parser uri:" + uri.toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mUploadMessage.onReceiveValue(new Uri[]{uri});
            } else {
                mUploadMessage.onReceiveValue(uri);
            }
            mUploadMessage = null;
        }
    }
}
