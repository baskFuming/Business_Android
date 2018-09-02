package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.MyShareBean;
import com.zwonline.top28.model.MyShareModel;
import com.zwonline.top28.view.IMyShareActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author YSG
 * @desc我的分享
 * @date 2017-12-25
 */
public class MySharePresenter extends BasePresenter<IMyShareActivity> {
    private MyShareModel myShareModel;
    private IMyShareActivity iMyShareActivity;

    public MySharePresenter(IMyShareActivity iMyShareActivity) {
        this.iMyShareActivity = iMyShareActivity;
        myShareModel = new MyShareModel();
    }

    //我的分享
    public void mMyShare(Context context, String uid, final int page) {
        try {
            Flowable<MyShareBean> flowable = myShareModel.MyShare(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyShareBean>(context) {
                        @Override
                        protected void onBaseNext(MyShareBean myShareBean) {
                            Log.e("myShareBean==", myShareBean.msg);
                            if (page == 1) {
                                if (myShareBean.data.size() > 0) {
                                    iMyShareActivity.showMyShare(true);
                                    iMyShareActivity.showMyShareDte(myShareBean.data);
                                } else {
                                    iMyShareActivity.showMyShare(false);
                                }
                            } else {
                                if (myShareBean.data.size() > 0) {
                                    iMyShareActivity.showMyShareDte(myShareBean.data);
                                } else {
                                    iMyShareActivity.noLoadMore();
                                }
                            }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //我的分享加载更多
    public void mMyShareLoad(Context context, String uid, final int page) {
        try {
            Flowable<MyShareBean> flowable = myShareModel.MyShare(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyShareBean>() {
                        @Override
                        public void onNext(MyShareBean myShareBean) {
                            Log.e("myShareBean==", myShareBean.msg);
                            if (page == 1) {
                                if (myShareBean.data.size() > 0) {
                                    iMyShareActivity.showMyShare(true);
                                    iMyShareActivity.showMyShareDte(myShareBean.data);
                                } else {
                                    iMyShareActivity.showMyShare(false);
                                }
                            } else {
                                if (myShareBean.data.size() > 0) {
                                    iMyShareActivity.showMyShareDte(myShareBean.data);
                                } else {
                                    iMyShareActivity.noLoadMore();
                                }
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
}
