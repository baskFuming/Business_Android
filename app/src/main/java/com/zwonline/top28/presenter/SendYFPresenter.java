package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.bean.HongBaoLeftCountBean;
import com.zwonline.top28.bean.HongBaoLogBean;
import com.zwonline.top28.bean.MyCollectBean;
import com.zwonline.top28.bean.RecommendTeamsBean;
import com.zwonline.top28.bean.SendYFBean;
import com.zwonline.top28.bean.YfRecordBean;
import com.zwonline.top28.model.SendYfModel;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.ISendYFActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 发鞅分
 */
public class SendYFPresenter extends BasePresenter<ISendYFActivity> {
    private SendYfModel sendYfModel;
    private ISendYFActivity iSendYFActivity;

    public SendYFPresenter(ISendYFActivity iSendYFActivity) {
        sendYfModel = new SendYfModel();
        this.iSendYFActivity = iSendYFActivity;
    }

    /**
     * 发送鞅分红包
     *
     * @param context
     * @param postscript
     * @param total_amount
     * @param total_package
     * @param random_flag
     */
    public void mSendYF(Context context, String postscript, String total_amount, String total_package, int random_flag) {
        try {

            Flowable<SendYFBean> flowable = sendYfModel.sendYf(context, postscript, total_amount, total_package, random_flag);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<SendYFBean>() {
                        @Override
                        public void onNext(SendYFBean sendYFBean) {
                            Log.i("sendYFBean==", sendYFBean.msg);
                            iSendYFActivity.showYfdata(sendYFBean);
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送商机币红包
     *
     * @param context
     * @param postscript
     * @param total_amount
     * @param total_package
     * @param random_flag
     */
    public void mBocHongBao(Context context, String postscript, String total_amount, String total_package, int random_flag) {
        try {

            Flowable<SendYFBean> flowable = sendYfModel.mSendBocHongBao(context, postscript, total_amount, total_package, random_flag);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<SendYFBean>() {
                        @Override
                        public void onNext(SendYFBean sendYFBean) {
                            Log.i("sendYFBean==", sendYFBean.msg);
                            iSendYFActivity.showYfdata(sendYFBean);
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void mSendYFs(Context context, String postscript, String total_amount, String total_package, int random_flag) {
        try {
            Flowable<SendYFBean> flowable = sendYfModel.sendYf(context, postscript, total_amount, total_package, random_flag);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<SendYFBean>(context) {
                        @Override
                        protected void onBaseNext(SendYFBean sendYFBean) {
                            iSendYFActivity.showYfdata(sendYFBean);
                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {
                            return true;
                        }

                        @Override
                        protected void onBaseComplete() {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 红包记录
     *
     * @param context
     * @param hongbao_id
     * @param page
     */
    public void mSnatchYangFen(final Context context, String hongbao_id, int page) {
        try {

            Flowable<HongBaoLogBean> flowable = sendYfModel.snatchYangFen(context, hongbao_id, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HongBaoLogBean>() {
                        @Override
                        public void onNext(HongBaoLogBean hongBaoLogBean) {
                            Log.i("hongBaoLogBean==", hongBaoLogBean.msg);
                            if (hongBaoLogBean != null && hongBaoLogBean.status == 1) {
                                iSendYFActivity.showSnatchYangFe(hongBaoLogBean.data);
                            } else {
                                ToastUtils.showToast(context, hongBaoLogBean.msg);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商机币红包记录
     *
     * @param context
     * @param hongbao_id
     * @param page
     */
    public void BocHongbaoLog(Context context, String hongbao_id, int page) {
        try {

            Flowable<HongBaoLogBean> flowable = sendYfModel.mBocHongbaoLog(context, hongbao_id, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HongBaoLogBean>() {
                        @Override
                        public void onNext(HongBaoLogBean hongBaoLogBean) {
                            Log.i("hongBaoLogBean==", hongBaoLogBean.msg);
                            iSendYFActivity.showSnatchYangFe(hongBaoLogBean.data);
                        }

                        @Override
                        public void onError(Throwable t) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 鞅分红包剩余量
     *
     * @param context
     * @param hongbao_id
     */
    public void mHongBaoLeftCount(Context context, String hongbao_id) {
        try {

            Flowable<HongBaoLeftCountBean> flowable = sendYfModel.hongBaoLeftCount(context, hongbao_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HongBaoLeftCountBean>() {
                        @Override
                        public void onNext(HongBaoLeftCountBean hongBaoLeftCountBean) {
                            Log.i("hongBaoLeftCountBean==", hongBaoLeftCountBean.msg);
                            iSendYFActivity.showHongBaoLeftCount(hongBaoLeftCountBean);
                        }

                        @Override
                        public void onError(Throwable t) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商机币红包剩余量
     *
     * @param context
     * @param hongbao_id
     */
    public void GetBocHongbaoLeftCount(Context context, String hongbao_id) {
        try {

            Flowable<HongBaoLeftCountBean> flowable = sendYfModel.mGetBocHongbaoLeftCount(context, hongbao_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HongBaoLeftCountBean>() {
                        @Override
                        public void onNext(HongBaoLeftCountBean hongBaoLeftCountBean) {
                            Log.i("hongBaoLeftCountBean==", hongBaoLeftCountBean.msg);
                            iSendYFActivity.showHongBaoLeftCount(hongBaoLeftCountBean);
                        }

                        @Override
                        public void onError(Throwable t) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 鞅分红包记录
     *
     * @param context
     */
    public void mYfRecord(Context context, int page) {
        try {

            Flowable<YfRecordBean> flowable = sendYfModel.yfHongBaoRecord(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<YfRecordBean>() {
                        @Override
                        public void onNext(YfRecordBean yfRecordBean) {
                            Log.i("sendYFBean==", yfRecordBean.msg);
                            iSendYFActivity.showYFRecord(yfRecordBean.data.list, yfRecordBean.data.totalReceiveHongbaoAmount);
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 商机币总红包记录
     *
     * @param context
     * @param page
     */
    public void BocRecord(Context context, int page) {
        try {

            Flowable<YfRecordBean> flowable = sendYfModel.mBocRecord(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<YfRecordBean>() {
                        @Override
                        public void onNext(YfRecordBean yfRecordBean) {
                            Log.i("sendYFBean==", yfRecordBean.msg);
                            iSendYFActivity.showYFRecord(yfRecordBean.data.list, yfRecordBean.data.totalReceiveHongbaoAmount);
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
