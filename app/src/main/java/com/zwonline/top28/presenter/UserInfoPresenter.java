package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.MyPageBean;
import com.zwonline.top28.bean.NoticeNotReadCountBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.model.UserInfoModel;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.IUserInfo;

import org.jsoup.select.Evaluator;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author YSG
 * @desc获取个人信息
 * @date ${Date}
 */
public class UserInfoPresenter extends BasePresenter<IUserInfo> {
    private UserInfoModel userInfoModel;
    private IUserInfo iUserInfo;
    private SharedPreferencesUtils sp;

    public UserInfoPresenter(IUserInfo iUserInfo) {
        this.iUserInfo = iUserInfo;
        userInfoModel = new UserInfoModel();
    }

    /**
     * 个人信息
     *
     * @param context
     */
    public void mUserInfo(final Context context) {
        try {
            Flowable<UserInfoBean> flowable = userInfoModel.UserInfo(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<UserInfoBean>() {
                        @Override
                        public void onNext(UserInfoBean userInfoBean) {
                            sp = SharedPreferencesUtils.getUtil();
                            boolean islogins = (boolean) sp.getKey(context, "islogin", false);

                            if (userInfoBean != null && userInfoBean.data != null) {
                                iUserInfo.showUserInfo(userInfoBean);
                            } else {
                                iUserInfo.showErro();
                            }

                        }

                        @Override
                        public void onError(Throwable t) {
                            iUserInfo.showErro();
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
     * 通告
     *
     * @param context
     */
    public void mNoticeNotReadCount(final Context context) {
        try {
            Flowable<NoticeNotReadCountBean> flowable = userInfoModel.NoticeNotReadCount(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<NoticeNotReadCountBean>() {
                        @Override
                        public void onNext(NoticeNotReadCountBean noticeNotReadCountBean) {
                            if (noticeNotReadCountBean.status == 1) {
                                iUserInfo.showNoticeNoRead(noticeNotReadCountBean);
                            } else {
                                ToastUtils.showToast(context, noticeNotReadCountBean.msg);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iUserInfo.showErro();
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
     * 个人中心菜单
     *
     * @param context
     */
    public void PersonCenterMenu(final Context context) {
        try {
            Flowable<MyPageBean> flowable = userInfoModel.mPersonCenterMenu(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyPageBean>(context) {

                        @Override
                        protected void onBaseNext(MyPageBean myPageBean) {
                            String status = String.valueOf(myPageBean.status);
                            if (myPageBean.status == 1) {
                                iUserInfo.showPersonCenterMenu(myPageBean.data);
                            }else {
                                ToastUtils.showToast(context,myPageBean.msg);
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
}
