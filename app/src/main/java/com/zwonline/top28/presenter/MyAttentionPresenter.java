package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.MyAttentionBean;
import com.zwonline.top28.model.MyAttentionModel;
import com.zwonline.top28.view.IMyAttentionActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author YSG
 * @desc我的关注
 * @date ${Date}
 */
public class MyAttentionPresenter extends BasePresenter<IMyAttentionActivity> {
    private MyAttentionModel myAttentionModel;
    private IMyAttentionActivity iMyAttentionActivity;

    public MyAttentionPresenter(IMyAttentionActivity iMyAttentionActivity) {
        this.iMyAttentionActivity = iMyAttentionActivity;
        myAttentionModel = new MyAttentionModel();
    }

    //我的关注
    public void mMyAttention(Context context, String uid, final int page) {
        try {
            Flowable<MyAttentionBean> flowable = myAttentionModel.myAttention(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyAttentionBean>(context) {
                        @Override
                        protected void onBaseNext(MyAttentionBean myAttentionBean) {
                            Log.e("myAttention===", myAttentionBean.msg);
                            if (page==1){
                                if (myAttentionBean.data.size() > 0) {
                                    iMyAttentionActivity.showMyAttention(true);
                                    iMyAttentionActivity.showMyAttentionData(myAttentionBean.data);
                                } else {
                                    iMyAttentionActivity.showMyAttention(false);

                                }
                            }else {
                                if (myAttentionBean.data.size()>0){
                                    iMyAttentionActivity.showMyAttentionData(myAttentionBean.data);
                                }else {
                                    iMyAttentionActivity.noData();
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

    //我的关注
    public void mAttention(Context context, final int page) {
        try {
            Flowable<MyAttentionBean> flowable = myAttentionModel.Attention(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyAttentionBean>(context) {
                        @Override
                        protected void onBaseNext(MyAttentionBean myAttentionBean) {
                            Log.e("myAttention===", myAttentionBean.msg);
                            if (page==1){
                                if (myAttentionBean.data.size() > 0) {
                                    iMyAttentionActivity.showMyAttention(true);
                                    iMyAttentionActivity.showMyAttentionData(myAttentionBean.data);
                                } else {
                                    iMyAttentionActivity.showMyAttention(false);

                                }
                            }else {
                                if (myAttentionBean.data.size()>0){
                                    iMyAttentionActivity.showMyAttentionData(myAttentionBean.data);
                                }else {
                                    iMyAttentionActivity.noData();
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
    //我的关注
    public void mMyAttentions(Context context, String uid, final int page) {
        try {
            Flowable<MyAttentionBean> flowable = myAttentionModel.myAttention(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyAttentionBean>() {
                        @Override
                        public void onNext(MyAttentionBean myAttentionBean) {
                            Log.e("myAttention===", myAttentionBean.msg);
                            if (page==1){
                                if (myAttentionBean.data.size() > 0) {
                                    iMyAttentionActivity.showMyAttention(true);
                                    iMyAttentionActivity.showMyAttentionData(myAttentionBean.data);
                                } else {
                                    iMyAttentionActivity.showMyAttention(false);

                                }
                            }else {
                                if (myAttentionBean.data.size()>0){
                                    iMyAttentionActivity.showMyAttentionData(myAttentionBean.data);
                                }else {
                                    iMyAttentionActivity.noData();
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

    //我的关注
    public void mAttentions(Context context, final int page) {
        try {
            Flowable<MyAttentionBean> flowable = myAttentionModel.Attention(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyAttentionBean>() {
                        @Override
                        public void onNext(MyAttentionBean myAttentionBean) {
                            Log.e("myAttention===", myAttentionBean.msg);
                            if (page==1){
                                if (myAttentionBean.data.size() > 0) {
                                    iMyAttentionActivity.showMyAttention(true);
                                    iMyAttentionActivity.showMyAttentionData(myAttentionBean.data);
                                } else {
                                    iMyAttentionActivity.showMyAttention(false);

                                }
                            }else {
                                if (myAttentionBean.data.size()>0){
                                    iMyAttentionActivity.showMyAttentionData(myAttentionBean.data);
                                }else {
                                    iMyAttentionActivity.noData();
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
