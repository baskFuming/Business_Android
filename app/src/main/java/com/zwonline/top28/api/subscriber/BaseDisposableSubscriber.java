package com.zwonline.top28.api.subscriber;


import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.zwonline.top28.api.exception.ApiException;
import com.zwonline.top28.api.progress.ProgressDialogHandler;
import com.zwonline.top28.tip.dialog.TipLoadDialog;
import com.zwonline.top28.tip.toast.ToastUtil;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 对请求失败的分类操作
 * Created by sdh on 2018/3/20.
 */

public abstract class BaseDisposableSubscriber<T> extends DisposableSubscriber {

    private static final String TAG = "BaseDisposableSubscriber";
    private Context context;
    private ProgressDialogHandler mProgressDialogHandler;
    private TipLoadDialog tipLoadDialog;
    private boolean isNeedProgress;
    private String titleMsg;
    public static final String LOADING_MSG = "玩命加载中...";

    protected abstract void onBaseNext(T data);

    protected abstract String getTitleMsg();

    protected abstract boolean isNeedProgressDialog();

    protected abstract void onBaseComplete();

    public BaseDisposableSubscriber(Context context) {
        this.context = context;
        if (null == tipLoadDialog) {
//        if (null == mProgressDialogHandler) {
            tipLoadDialog = new TipLoadDialog(context);
//            mProgressDialogHandler = new ProgressDialogHandler(context, true);
        }
    }

    private void showProgressDialog() {
        if (tipLoadDialog != null) {
            tipLoadDialog.setMsgAndType(LOADING_MSG, TipLoadDialog.ICON_TYPE_LOADING).show();
//            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG, getTitleMsg()).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (tipLoadDialog != null) {
            tipLoadDialog.dismiss();
//            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            tipLoadDialog = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //显示进度条
        if (isNeedProgressDialog()) {
            showProgressDialog();
        }
    }

    @Override
    public void onNext(final Object value) {
        //成功
        if (null != value) {
            onBaseNext((T) value);
        }
    }

    @Override
    public void onError(Throwable t) {
        //关闭进度条
        if (isNeedProgressDialog()) {
            dismissProgressDialog();
        }
        //拼接错误
        StringBuilder sb = new StringBuilder();
        sb.append("请求失败");
        if (t instanceof NetworkErrorException || t instanceof UnknownHostException || t instanceof ConnectException) {
            sb.append(":网络异常");
        } else if (t instanceof SocketTimeoutException || t instanceof InterruptedIOException || t instanceof TimeoutException) {
            sb.append(":请求超时");
        } else if (t instanceof JsonSyntaxException) {
//            sb.append("请求不合法");
        } else if (t instanceof JsonParseException
                || t instanceof JSONException
                || t instanceof ParseException) {   //  解析错误
            sb.append(":解析错误");
        } else if (t instanceof ApiException) {
            if (((ApiException) t).isTokenExpried()) {
                sb.append(":Token出错");
            }
        } else {
            ToastUtil.showToastSafe(t.getMessage());
            return;
        }
        Log.e(TAG, "onError: " + sb.toString());
        ToastUtil.showToastSafe(sb.toString());

    }

    @Override
    public void onComplete() {
        //关闭进度条
        if (isNeedProgressDialog()) {
            dismissProgressDialog();
            onBaseComplete();
        }
    }
}
