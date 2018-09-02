package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.CompanyBean;
import com.zwonline.top28.bean.ExamineChatBean;
import com.zwonline.top28.bean.MyIssueBean;
import com.zwonline.top28.model.CompanyModel;
import com.zwonline.top28.view.ICompanyActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;


/**
 * 描述：企业主页
 *
 * @author YSG
 * @date 2018/1/2
 */
public class CompanyPresenter extends BasePresenter<ICompanyActivity> {
    private CompanyModel companyModel;
    private ICompanyActivity iCompanyActivity;

    public CompanyPresenter(ICompanyActivity iCompanyActivity) {
        this.iCompanyActivity = iCompanyActivity;
        companyModel = new CompanyModel();
    }

    //企业主页信息
    public void mCompany(Context context, String id) {
        try {
            Flowable<CompanyBean> flowable = companyModel.Company(context, id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<CompanyBean>(context) {
                        @Override
                        protected void onBaseNext(CompanyBean companyBean) {
                            if (companyBean != null && companyBean.data.post_page != null && companyBean.data.post_page.size() > 0) {
                                iCompanyActivity.showCompany(companyBean);
                            } else {
                                iCompanyActivity.showErro();
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

    //企业文章
    public void mCompany_Article(Context context, String uid,int page) {
        try {

            Flowable<MyIssueBean> flowable = companyModel.myIssue(context, uid,page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyIssueBean>() {
                        @Override
                        public void onNext(MyIssueBean myIssueBean) {
//                            if (myIssueBean.data != null && myIssueBean.data.size() > 0) {
//                                iCompanyActivity.showColmpanyArticle(myIssueBean.data);
//                            } else {
//                                iCompanyActivity.showErro();
//                            }
                            iCompanyActivity.showColmpanyArticle(myIssueBean.data);
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
    public void mAttention(Context context, String type, String pid, String allow_be_call) {
        try {
            Flowable<AttentionBean> flowable = companyModel.attention(context, type, pid, allow_be_call);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {

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
            Flowable<AttentionBean> flowable = companyModel.unAttention(context, type, uid);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {

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

    //检查是否和某人聊过天接口
    public void mExamineChat(Context context, String uid) {
        try {
            Flowable<ExamineChatBean> flowable = companyModel.ExamineChat(context, uid);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<ExamineChatBean>() {
                        @Override
                        public void onNext(ExamineChatBean attentionBean) {
                            iCompanyActivity.showExamineChat(attentionBean.data);
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

    //在线聊天
    public void mOnLineChat(Context context, String uid,String projectId,String kefuUid) {
        try {
            Flowable<AmountPointsBean> flowable = companyModel.OnLineChat(context, uid,projectId,kefuUid);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AmountPointsBean>() {
                        @Override
                        public void onNext(AmountPointsBean attentionBean) {
                            iCompanyActivity.showOnLineChat(attentionBean);
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
