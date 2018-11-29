package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.ExamineChatBean;
import com.zwonline.top28.bean.MyIssueBean;
import com.zwonline.top28.bean.MyShareBean;
import com.zwonline.top28.bean.PersonageInfoBean;
import com.zwonline.top28.bean.RealBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.model.HomePageModel;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.IHomePageActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author YSG
 * @desc个人主页
 * @date ${Date}
 */
public class HomePagePresenter extends BasePresenter<IHomePageActivity> {
    public HomePageModel homePageModel;
    public IHomePageActivity iHomePageActivity;

    public HomePagePresenter(IHomePageActivity iHomePageActivity) {
        this.iHomePageActivity = iHomePageActivity;
        homePageModel = new HomePageModel();
    }

    //用户主页信息
    public void mCompany(final Context context, String uid) {
        try {
            Flowable<PersonageInfoBean> flowable = homePageModel.Company(context, uid);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<PersonageInfoBean>() {
                        @Override
                        public void onNext(PersonageInfoBean companyBean) {
                            if (companyBean.status == 1) {
                                if (companyBean != null && companyBean.data != null) {

                                    iHomePageActivity.showCompany(companyBean);
                                } else {
                                    iHomePageActivity.onErro();
                                }
                            } else {
                                ToastUtils.showToast(context, companyBean.msg);
                            }

//                            EventBus.getDefault().post(companyBean);
                        }

                        @Override
                        public void onError(Throwable t) {
                            iHomePageActivity.onErro();
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
    public void mAttention(Context context, String type, String uid, String allow_be_call) {
        try {
            Flowable<AttentionBean> flowable = homePageModel.attention(context, type, uid, allow_be_call);
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
            Flowable<AttentionBean> flowable = homePageModel.unFollowAttention(context, type, uid);
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


    //我的分享
    public void mMyShare(Context context, String uid, final int page) {
        try {
            Flowable<MyShareBean> flowable = homePageModel.MyShare(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyShareBean>(context) {

                        @Override
                        protected void onBaseNext(MyShareBean myShareBean) {
                            Log.e("myShareBean==", myShareBean.msg);
                            if (page == 1) {
                                if (myShareBean.data.size() > 0) {
                                    iHomePageActivity.showMyShare(true);
                                    iHomePageActivity.showMyShareDte(myShareBean.data);
                                } else {
                                    iHomePageActivity.showMyShare(false);
                                }
                            } else {
                                if (myShareBean.data.size() > 0) {
                                    iHomePageActivity.showMyShareDte(myShareBean.data);
                                } else {
                                    iHomePageActivity.shareNoLoadMore();
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
            Flowable<MyShareBean> flowable = homePageModel.MyShare(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyShareBean>() {
                        @Override
                        public void onNext(MyShareBean myShareBean) {
                            Log.e("myShareBean==", myShareBean.msg);
                            if (page == 1) {
                                if (myShareBean.data.size() > 0) {
                                    iHomePageActivity.showMyShare(true);
                                    iHomePageActivity.showMyShareDte(myShareBean.data);
                                } else {
                                    iHomePageActivity.showMyShare(false);
                                }
                            } else {
                                if (myShareBean.data.size() > 0) {
                                    iHomePageActivity.showMyShareDte(myShareBean.data);
                                } else {
                                    iHomePageActivity.shareNoLoadMore();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iHomePageActivity.onErro();
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
            Flowable<ExamineChatBean> flowable = homePageModel.ExamineChat(context, uid);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<ExamineChatBean>() {
                        @Override
                        public void onNext(ExamineChatBean attentionBean) {
                            iHomePageActivity.showExamineChat(attentionBean.data);
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
    public void mOnLineChat(Context context, String uid) {
        try {
            Flowable<AmountPointsBean> flowable = homePageModel.OnLineChat(context, uid);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AmountPointsBean>() {
                        @Override
                        public void onNext(AmountPointsBean attentionBean) {
                            iHomePageActivity.showOnLineChat(attentionBean);
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

    //微信分享名片
    public void cardShareWXin(Context context, String show_realname, String show_telephone, String show_weixin, String show_address, String show_enterprise, String show_position) {
        try {
            Flowable<RealBean> flowable = homePageModel.shareWxin(context, show_realname, show_telephone, show_weixin, show_address, show_enterprise, show_position);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<RealBean>() {
                        @Override
                        public void onNext(RealBean realBean) {
                            iHomePageActivity.showShareWXin(realBean);
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

    //设置个人资料
    public void mSetting(final Context context, String nick_name,
                         String real_name, int sex, String age,
                         String address, String favourite_industry,
                         String bio, String weixin, String email, String telephone, String job_cate_pid, String enterprise, String position) {
        try {

            Flowable<SettingBean> flowable = homePageModel.mSetingModel(context, nick_name, real_name, sex, age, address,
                    favourite_industry, bio, weixin, email, telephone, job_cate_pid, enterprise, position);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<SettingBean>() {
                        @Override
                        public void onNext(SettingBean headBean) {
                            if (headBean.status == 1) {
                                iHomePageActivity.isSucceed();
                            } else {
                                iHomePageActivity.onErro();
                            }
                            iHomePageActivity.showSetting(headBean);
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

    //获取用户信息
    public void mUserInfo(Context context) {
        try {
            Flowable<UserInfoBean> flowable = homePageModel.UserInfo(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<UserInfoBean>() {
                        @Override
                        public void onNext(UserInfoBean userInfoBean) {
                            if (userInfoBean != null && userInfoBean.data != null) {
                                iHomePageActivity.showUserInfo(userInfoBean);
                            } else {
                                iHomePageActivity.onErro();
                            }

                        }

                        @Override
                        public void onError(Throwable t) {
                            iHomePageActivity.onErro();
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
