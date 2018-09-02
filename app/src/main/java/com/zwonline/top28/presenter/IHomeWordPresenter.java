package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.MyIssueBean;
import com.zwonline.top28.model.HomePageModel;
import com.zwonline.top28.view.IHomeWordActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class IHomeWordPresenter extends BasePresenter<IHomeWordActivity> {
    public HomePageModel homePageModel;
    public IHomeWordActivity iHomePageActivity;

    public IHomeWordPresenter(IHomeWordActivity iHomePageActivity) {
        this.iHomePageActivity = iHomePageActivity;
        homePageModel = new HomePageModel();
    }
    //我的发布
    public void mMyissue(Context context, String uid, final int page) {
        try {
            Flowable<MyIssueBean> flowable = homePageModel.myIssue(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyIssueBean>(context) {
                        @Override
                        protected void onBaseNext(MyIssueBean myIssueBean) {
                            Log.e("myIssueBean==", myIssueBean.msg);
                            if (page == 1) {
                                if (myIssueBean.data.size() > 0) {
                                    iHomePageActivity.showMyIssue(true);
//                                EventBus.getDefault().post(true);
                                    iHomePageActivity.showMyIssueDate(myIssueBean.data);
                                } else {
                                    iHomePageActivity.showMyIssue(false);
                                }
                            } else {
                                if (myIssueBean.data.size() > 0) {
                                    iHomePageActivity.showMyIssueDate(myIssueBean.data);
                                } else {
                                    iHomePageActivity.issueNoLoadMore();
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


    //我的发布加载更多
    public void mMyissueLoad(Context context, String uid, final int page) {
        try {
            Flowable<MyIssueBean> flowable = homePageModel.myIssue(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyIssueBean>() {

                        @Override
                        public void onNext(MyIssueBean myIssueBean) {
                            Log.e("myIssueBean==", myIssueBean.msg);
                            if (page == 1) {
                                if (myIssueBean.data.size() > 0) {
                                    iHomePageActivity.showMyIssue(true);
//                                EventBus.getDefault().post(true);
                                    iHomePageActivity.showMyIssueDate(myIssueBean.data);
                                } else {
                                    iHomePageActivity.showMyIssue(false);
                                }
                            } else {
                                if (myIssueBean.data.size() > 0) {
                                    iHomePageActivity.showMyIssueDate(myIssueBean.data);
                                } else {
                                    iHomePageActivity.issueNoLoadMore();
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
