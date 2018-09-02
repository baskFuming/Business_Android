package com.zwonline.top28.api.subscriber;

import android.content.Context;
import android.widget.Toast;

import com.zwonline.top28.api.progress.ProgressCancelListener;
import com.zwonline.top28.api.progress.ProgressDialogHandler;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/** BaseSubscriber
 * Created by sdh on 2018/3/20.
 */

public abstract class BaseSubscriber <T>  implements Subscriber<T> , ProgressCancelListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;

    private void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {

    }

    /**
     *  订阅开始时调用
     * 显示ProgressDialog
     * @param s
     */
    @Override
    public void onSubscribe(Subscription s) {
        showProgressDialog();
    }

    /**
     *  将onNext方法中的返回结果交给Activity或Fragment自己处理
     *  创建Subscriber时的泛型类型
     * @param t
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        dismissProgressDialog();
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
        Toast.makeText(context, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
    }
}
