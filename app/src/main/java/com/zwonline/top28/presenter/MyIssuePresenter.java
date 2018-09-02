package com.zwonline.top28.presenter;


import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.MyIssueBean;
import com.zwonline.top28.model.MyIssueModel;
import com.zwonline.top28.view.IMyIssueActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 描述：我的发布
 *
 * @author YSG
 * @date 2017/12/25
 */
public class MyIssuePresenter extends BasePresenter<IMyIssueActivity> {
    private MyIssueModel myIssueModel;
    private IMyIssueActivity iMyIssueActivity;

    public MyIssuePresenter(IMyIssueActivity iMyIssueActivity) {
        this.iMyIssueActivity = iMyIssueActivity;
        myIssueModel = new MyIssueModel();
    }

    //我的发布
    public void mMyissue(Context context, String uid, final int page) {
        try {
            Flowable<MyIssueBean> flowable = myIssueModel.myIssue(context, uid,page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyIssueBean>(context) {
                        @Override
                        protected void onBaseNext(MyIssueBean myIssueBean) {
                            Log.e("myIssueBean==", myIssueBean.msg);
                            if (page==1){
                                if (myIssueBean.data.size() > 0) {
                                    iMyIssueActivity.showMyIssue(true);
                                    iMyIssueActivity.showMyIssueDate(myIssueBean.data);
                                } else {
                                    iMyIssueActivity.showMyIssue(false);
                                }
                            }else {
                                if (myIssueBean.data.size() > 0) {
                                    iMyIssueActivity.showMyIssueDate(myIssueBean.data);
                                } else {
                                    iMyIssueActivity.noLoadMore();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //我的发布
    public void mMyissues(Context context, final int page) {
        try {
            Flowable<MyIssueBean> flowable = myIssueModel.myIssues(context,page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyIssueBean>(context) {
                        @Override
                        protected void onBaseNext(MyIssueBean myIssueBean) {
                            Log.e("myIssueBean==", myIssueBean.msg);
                            if (page==1){
                                if (myIssueBean.data.size() > 0) {
                                    iMyIssueActivity.showMyIssue(true);
                                    iMyIssueActivity.showMyIssueDate(myIssueBean.data);
                                } else {
                                    iMyIssueActivity.showMyIssue(false);
                                }
                            }else {
                                if (myIssueBean.data.size() > 0) {
                                    iMyIssueActivity.showMyIssueDate(myIssueBean.data);
                                } else {
                                    iMyIssueActivity.noLoadMore();
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

    //我的发布
    public void mMyissueLoad(Context context, String uid, final int page) {
        try {
            Flowable<MyIssueBean> flowable = myIssueModel.myIssue(context, uid,page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyIssueBean>() {
                        @Override
                        public void onNext(MyIssueBean myIssueBean) {
                            Log.e("myIssueBean==", myIssueBean.msg);
                            if (page==1){
                                if (myIssueBean.data.size() > 0) {
                                    iMyIssueActivity.showMyIssue(true);
                                    iMyIssueActivity.showMyIssueDate(myIssueBean.data);
                                } else {
                                    iMyIssueActivity.showMyIssue(false);
                                }
                            }else {
                                if (myIssueBean.data.size() > 0) {
                                    iMyIssueActivity.showMyIssueDate(myIssueBean.data);
                                } else {
                                    iMyIssueActivity.noLoadMore();
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

    //我的发布
    public void mMyissuesLoad(Context context, final int page) {
        try {
            Flowable<MyIssueBean> flowable = myIssueModel.myIssues(context,page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyIssueBean>() {
                        @Override
                        public void onNext(MyIssueBean myIssueBean) {
                            Log.e("myIssueBean==", myIssueBean.msg);
                            if (page==1){
                                if (myIssueBean.data.size() > 0) {
                                    iMyIssueActivity.showMyIssue(true);
                                    iMyIssueActivity.showMyIssueDate(myIssueBean.data);
                                } else {
                                    iMyIssueActivity.showMyIssue(false);
                                }
                            }else {
                                if (myIssueBean.data.size() > 0) {
                                    iMyIssueActivity.showMyIssueDate(myIssueBean.data);
                                } else {
                                    iMyIssueActivity.noLoadMore();
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
