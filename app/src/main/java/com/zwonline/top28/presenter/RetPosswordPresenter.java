package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.LoginBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.model.RetPossWordModel;
import com.zwonline.top28.view.IRetPossword;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author YSG
 * @desc密码重置
 * @date ${Date}
 */
public class RetPosswordPresenter extends BasePresenter<IRetPossword> {
    private RetPossWordModel retPossWordModel;
    private IRetPossword iRetPossword;

    public RetPosswordPresenter(IRetPossword iRetPossword) {
        this.iRetPossword = iRetPossword;
        retPossWordModel = new RetPossWordModel();
    }

    //密码重置
    public void mRetPossWord(Context context, String possword) {
        try {
            Flowable<SettingBean> flowable = retPossWordModel.RetPoss(context, possword);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<SettingBean>(context) {

                        @Override
                        protected void onBaseNext(SettingBean headBean) {
                            iRetPossword.showRetPossWord(headBean);
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

    //账号密码登录
    public void loginNumber(Context context,String mobile, String password) {
        try {
            Flowable<LoginBean> flowable = retPossWordModel.loginUserNumber(context,mobile, password);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<LoginBean>() {
                        @Override
                        public void onNext(LoginBean loginBean) {
                            if (loginBean != null && loginBean.getData() != null) {
                                iRetPossword.Success(loginBean);
                            } else {
                                iRetPossword.onErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iRetPossword.onErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取个人信息
    public void mUserInfo(final Context context) {
        try {
            Flowable<UserInfoBean> flowable = retPossWordModel.UserInfo(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<UserInfoBean>() {
                        @Override
                        public void onNext(UserInfoBean userInfoBean) {

                            if (userInfoBean != null && userInfoBean.data != null) {
                                iRetPossword.showUserInfo(userInfoBean);
                            } else {
                                iRetPossword.onErro();
                            }

                        }

                        @Override
                        public void onError(Throwable t) {
                            iRetPossword.onErro();
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
