package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.MyFansBean;
import com.zwonline.top28.model.MyFansModel;
import com.zwonline.top28.view.IMyFansActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;


/**
 * 描述：
 *
 * @author YSG
 * @date 2017/12/22
 */
public class MyFansPresenter extends BasePresenter<IMyFansActivity> {
    private MyFansModel myFansModel;
    private IMyFansActivity iMyFansActivity;

    public MyFansPresenter(IMyFansActivity iMyFansActivity) {
        this.iMyFansActivity = iMyFansActivity;
        myFansModel = new MyFansModel();
    }

    //    我的粉丝
    public void mMyFanses(Context context, String filter, int page) {
        try {
            Flowable<MyFansBean> flowable = myFansModel.mMyFanses(context, filter, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyFansBean>() {
                        @Override
                        public void onNext(MyFansBean myFansBean) {
                            if (myFansBean.data.size() > 0) {
                                iMyFansActivity.showMyFans(true);
                                iMyFansActivity.showMyFansDate(myFansBean.data);
                            } else {
                                iMyFansActivity.showMyFans(false);
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

    //我的粉丝
    public void mMyFans(Context context, String uid, int page) {
        try {
            Flowable<MyFansBean> flowable = myFansModel.mFans(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyFansBean>(context) {
                        @Override
                        protected void onBaseNext(MyFansBean myFansBean) {
                            Log.e("myFansBean==", myFansBean.msg);
                            if (myFansBean.data.size() > 0) {
                                iMyFansActivity.showMyFans(true);
                                iMyFansActivity.showMyFansDate(myFansBean.data);
                            } else {
                                iMyFansActivity.showMyFans(false);
                            }
                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {
                            return false;
                        }

                        @Override
                        protected void onBaseComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //个人主页的粉丝
    public void mFans(Context context, String uid, final int page) {
        try {
            Flowable<MyFansBean> flowable = myFansModel.mFans(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyFansBean>(context) {
                        @Override
                        protected void onBaseNext(MyFansBean myFansBean) {
                            Log.e("myFansBean==", myFansBean.msg);
                            if (page == 1) {
                                if (myFansBean.data.size() > 0) {
                                    iMyFansActivity.showMyFans(true);
                                    iMyFansActivity.showMyFansDate(myFansBean.data);
                                } else {
                                    iMyFansActivity.showMyFans(false);
                                }
                            } else {
                                if (myFansBean.data.size() > 0) {
                                    iMyFansActivity.showMyFansDate(myFansBean.data);
                                } else {
                                    iMyFansActivity.noLoadMore();
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

    //个人主页的粉丝加载更多
    public void mFansMore(Context context, String uid, final int page) {
        try {
            Flowable<MyFansBean> flowable = myFansModel.mFans(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyFansBean>() {
                        @Override
                        public void onNext(MyFansBean myFansBean) {
                            Log.e("myFansBean==", myFansBean.msg);
                            if (page == 1) {
                                if (myFansBean.data.size() > 0) {
                                    iMyFansActivity.showMyFans(true);
                                    iMyFansActivity.showMyFansDate(myFansBean.data);
                                } else {
                                    iMyFansActivity.showMyFans(false);
                                }
                            } else {
                                if (myFansBean.data.size() > 0) {
                                    iMyFansActivity.showMyFansDate(myFansBean.data);
                                } else {
                                    iMyFansActivity.noLoadMore();
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

    //关注
    public void mAttentions(Context context, String type, String uid, String allow_be_call) {
        try {
            Flowable<AttentionBean> flowable = myFansModel.attention(context, type, uid, allow_be_call);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {
                            iMyFansActivity.showAttention(attentionBean);
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

    //取消关注
    public void mUnAttention(Context context, String type, String uid) {
        try {
            Flowable<AttentionBean> flowable = myFansModel.UnAttention(context, type, uid);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {
                            iMyFansActivity.showUnAttention(attentionBean);
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
