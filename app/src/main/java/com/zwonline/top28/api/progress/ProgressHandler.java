package com.zwonline.top28.api.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by sdh on 2018/3/20.
 */

public class ProgressHandler {
    public static final int SHOW_PROGRESS = 0;
    public static final int DISMISS_PROGRESS = 1;
    private ProgressDialog mDialog;
    private Context mContext;
    private ProgressCancelListener mProgressCancelListener;
    private boolean cancelable;
    private String mText;

    public ProgressHandler(Context context, ProgressCancelListener listener, boolean cancelable, String text){
        this.mContext = context;
        mProgressCancelListener = listener;
        this.cancelable = cancelable;
        this.mText = text;
    }

    public void initProgressDialog(){
        if(mDialog == null){
            mDialog = new ProgressDialog(mContext);
            mDialog.setCancelable(cancelable);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setMessage(mText);
            if(cancelable){
                mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }
            if(!mDialog.isShowing()){
                mDialog.show();//显示进度条
            }
        }
    }

    public void dismissProgressDialog(){
        if(mDialog!=null){
            mDialog.dismiss();//取消进度条
            mDialog = null;
        }
    }
}
