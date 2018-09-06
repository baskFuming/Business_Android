package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.InforNoticeBean;
import com.zwonline.top28.bean.InforNoticeCleanBean;
import com.zwonline.top28.bean.TipBean;
import com.zwonline.top28.model.HomePageModel;
import com.zwonline.top28.view.InforNoticeActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InForNoticePresenter extends BasePresenter<InforNoticeActivity> {
    public HomePageModel homePageModel;
    public InforNoticeActivity iHomePageActivity;

    public InForNoticePresenter(InforNoticeActivity iHomePageActivity) {
        this.iHomePageActivity = iHomePageActivity;
        homePageModel = new HomePageModel();
    }

    //通知列表
    public void InforNoticePageList(Context context, final int page) {
        try {
            Flowable<InforNoticeBean> flowable = homePageModel.inForNotice(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<InforNoticeBean>(context) {
                        @Override
                        protected void onBaseNext(InforNoticeBean myIssueBean) {
                            iHomePageActivity.inForNoticeList(myIssueBean.data);

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

    //通知列表
    public void InforNoticePageCleanList(Context context, final String page) {
        try {
            Flowable<InforNoticeCleanBean> flowable = homePageModel.inForNoticeClean(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<InforNoticeCleanBean>(context) {
                        @Override
                        protected void onBaseNext(InforNoticeCleanBean data) {
                            iHomePageActivity.inForNoticeCleanList(data);
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

    //是否读取
    public void InforNoticePageListTip(Context context, final int page) {
        try {
            Flowable<TipBean> flowable = homePageModel.inForNoticeTip(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<TipBean>(context) {
                        @Override
                        protected void onBaseNext(TipBean myIssueBean) {
                            iHomePageActivity.inForNoticeTip(myIssueBean);
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

}
