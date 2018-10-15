package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.LoginBean;
import com.zwonline.top28.bean.LoginWechatBean;
import com.zwonline.top28.bean.ShortMessage;
import com.zwonline.top28.model.LoginModel;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.view.ILoginActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/7 13:48
 */

public class LoginPresenter extends BasePresenter<ILoginActivity> {
    private SharedPreferencesUtils sp;
    private Context context;
    public ILoginActivity iLoginActivity;
    public LoginModel model;

    public LoginPresenter(ILoginActivity iLoginActivity, Context context) {
        this.iLoginActivity = iLoginActivity;
        model = new LoginModel();
        this.sp = (SharedPreferencesUtils) SharedPreferencesUtils.getUtil();
        this.context = context;
    }

    //账号密码登录
    public void loginNumber(String mobile, String password) {
        try {
            Flowable<LoginBean> flowable = model.loginUserNumber(context, mobile, password);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<LoginBean>() {

                        @Override
                        public void onNext(LoginBean loginBean) {
                            if (loginBean != null && loginBean.getData() != null) {
                                iLoginActivity.Success(loginBean);
                            } else {
                                iLoginActivity.onErro();
                            }
                            if (loginBean.getStatus() == 1) {
                                //登录云信
                                model.doLogin(loginBean.getData().getYunxin().getAccount(), loginBean.getData().getYunxin().getToken());
                                sp.insertKey(context, "account", loginBean.getData().getYunxin().getAccount());
                                sp.insertKey(context, "token", loginBean.getData().getYunxin().getToken());
                                sp.insertKey(context, "dialog", loginBean.getDialog());
                            }
                            iLoginActivity.isSuccess(loginBean.getStatus(), loginBean.getDialog(),
                                    loginBean.getData().getYunxin().getToken(),
                                    loginBean.getData().getYunxin().getAccount()
                            );
                            iLoginActivity.getToken(loginBean.getDialog());
                        }

                        @Override
                        public void onError(Throwable t) {
                            iLoginActivity.onErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //验证忘记密码手机验证码
    public void mforGetPossword(Context context, String phone, String code, String token) {
        Flowable<LoginBean> flowable = model.forGetPossword(context, phone, code, token);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<LoginBean>() {

                    @Override
                    public void onNext(LoginBean headBean) {
                        iLoginActivity.showForgetPossword(headBean);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //获取手机验证码
    public void getPhoneCode(String phone, String type, String token, String country_code) {
        Flowable<ShortMessage> flowable = model.getPhoneCode(phone, type, token, country_code);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseDisposableSubscriber<ShortMessage>(context) {

                    @Override
                    protected void onBaseNext(ShortMessage shortMessage) {
                        Log.i("xxx", shortMessage.getMsg() + ":" + shortMessage.getDialog());
                        if (shortMessage.getStatus() == 1 && shortMessage.getDialog() == null) {
                            sp.insertKey(context, "dialog", shortMessage.getDialog());
                        }
                        iLoginActivity.getToken(shortMessage.getDialog());
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
    }


    //获取手机验证码登录
    public void loginVerify(String mobile, String verify, String token) {
        try {
            Flowable<LoginBean> flowable = model.loginUserVerify(context, mobile, verify, token);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<LoginBean>() {

                        @Override
                        public void onNext(LoginBean loginBean) {
                            if (loginBean != null && loginBean.getData() != null) {
                                iLoginActivity.Success(loginBean);
                                if (loginBean.getStatus() == 1) {
                                    //登录云信
                                    model.doLogin(loginBean.getData().getYunxin().getAccount(), loginBean.getData().getYunxin().getToken());
                                    sp.insertKey(context, "account", loginBean.getData().getYunxin().getAccount());
                                    sp.insertKey(context, "token", loginBean.getData().getYunxin().getToken());
                                    iLoginActivity.isSuccess(loginBean.getStatus(), loginBean.getDialog(),
                                            loginBean.getData().getYunxin().getToken(),
                                            loginBean.getData().getYunxin().getAccount()
                                    );
                                }

                            } else {
                                iLoginActivity.showErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iLoginActivity.showErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取手机验证码登录有邀请码的
    public void loginVerifys(String mobile, String verify, String token, String incode) {
        try {
            Flowable<LoginBean> flowable = model.loginUserVerifys(context, mobile, verify, token, incode);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<LoginBean>() {
                        @Override
                        public void onNext(LoginBean loginBean) {
                            if (loginBean != null && loginBean.getData() != null) {
                                iLoginActivity.Success(loginBean);
                                if (loginBean.getStatus() == 1) {
                                    //登录云信
                                    model.doLogin(loginBean.getData().getYunxin().getAccount(), loginBean.getData().getYunxin().getToken());
                                    sp.insertKey(context, "account", loginBean.getData().getYunxin().getAccount());
                                    sp.insertKey(context, "token", loginBean.getData().getYunxin().getToken());
                                    iLoginActivity.isSuccess(loginBean.getStatus(), loginBean.getDialog(),
                                            loginBean.getData().getYunxin().getToken(),
                                            loginBean.getData().getYunxin().getAccount()
                                    );
                                }

                            } else {
                                iLoginActivity.showErro();
                            }

                        }

                        @Override
                        public void onError(Throwable t) {
                            iLoginActivity.showErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //微信登录
    public void loginWechatListen(final Context context, String union_id, String open_id, String gender, String nickname, String avatar, String country_code) {
        try {
            Flowable<LoginWechatBean> flowable = model.loginWechat(context, union_id, open_id, gender, nickname, avatar, country_code);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<LoginWechatBean>() {
                        @Override
                        public void onNext(LoginWechatBean loginWechatBean) {
                            if (loginWechatBean != null && loginWechatBean.getData() != null) {
                                iLoginActivity.wecahtSuccess(loginWechatBean);
                                if (loginWechatBean.getStatus() == 1) {
                                    //登录云信
                                    model.doLogin(loginWechatBean.getData().getYunxin().getAccount(), loginWechatBean.getData().getYunxin().getToken());
                                    sp.insertKey(context, "account", loginWechatBean.getData().getYunxin().getAccount());
                                    sp.insertKey(context, "token", loginWechatBean.getData().getYunxin().getToken());
                                    iLoginActivity.isSuccess(loginWechatBean.getStatus(), loginWechatBean.getDialog(),
                                            loginWechatBean.getData().getYunxin().getToken(),
                                            loginWechatBean.getData().getYunxin().getAccount()
                                    );
                                    iLoginActivity.getToken(loginWechatBean.getDialog());
                                }
                            } else {
                                iLoginActivity.onErro();

                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iLoginActivity.onErro();
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
