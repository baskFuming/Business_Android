package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.LoginWechatBean;
import com.zwonline.top28.bean.RegisterBean;
import com.zwonline.top28.bean.ShortMessage;
import com.zwonline.top28.model.RegisterModel;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.view.IRegisterActivity;

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

public class RegisterPresenter extends BasePresenter<IRegisterActivity> {
    private IRegisterActivity iRegisterActivity;
    private RegisterModel model;
    private SharedPreferencesUtils sp;
    private Context context;

    public RegisterPresenter(IRegisterActivity iRegisterActivity, Context context) {
        this.iRegisterActivity = iRegisterActivity;
        model = new RegisterModel();
        this.sp = (SharedPreferencesUtils) SharedPreferencesUtils.getUtil();
        this.context = context;
    }


    //获取手机验证码
    public void getPhoneCode(String phone, String type,String token,String country_code) {
        Flowable<ShortMessage> flowable = model.getPhoneCode(phone, type,token,country_code);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseDisposableSubscriber<ShortMessage>(context) {

                    @Override
                    protected void onBaseNext(ShortMessage shortMessage) {
                        iRegisterActivity.showStatus(shortMessage);
                        iRegisterActivity.getToken(shortMessage.getDialog());
                        Log.i("xxx", shortMessage.getMsg() + ":" + shortMessage.getDialog());
                        if (shortMessage.getStatus() == 1 && shortMessage.getDialog() == null) {
                            sp.insertKey(context, "dialog", shortMessage.getDialog());
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
    }

    //注册用户
    public void inject(String mobile, String smscode, String password, String passwordVerify, String token) {
        try {
            Flowable<RegisterBean> flowable = model.registerUser(context,mobile, smscode, password, passwordVerify, token);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<RegisterBean>() {
                        @Override
                        public void onNext(RegisterBean registerBean) {
                            if (registerBean != null && registerBean.getData() != null && registerBean.getData().equals("")) {

                                Log.i("xxx", registerBean.getData().getYunxin().getToken());
                                iRegisterActivity.isSuccess(registerBean.getStatus());
                            } else {
                                iRegisterActivity.onErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iRegisterActivity.onErro();
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
            Flowable<LoginWechatBean> flowable = model.loginWechat(context,union_id, open_id, gender, nickname,avatar,country_code);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<LoginWechatBean>() {
                        @Override
                        public void onNext(LoginWechatBean loginWechatBean) {
                            if (loginWechatBean!=null&&loginWechatBean.getData()!=null){
                                iRegisterActivity.loginShowWechat(loginWechatBean);
                                if (loginWechatBean.getStatus()==1){
                                    //登录云信
                                    model.doLogin(loginWechatBean.getData().getYunxin().getAccount(), loginWechatBean.getData().getYunxin().getToken());
                                    sp.insertKey(context, "account", loginWechatBean.getData().getYunxin().getAccount());
                                    sp.insertKey(context, "token", loginWechatBean.getData().getYunxin().getToken());
                                    iRegisterActivity.isSuccess(loginWechatBean.getStatus(), loginWechatBean.getDialog(),
                                            loginWechatBean.getData().getYunxin().getToken(),
                                            loginWechatBean.getData().getYunxin().getAccount()
                                    );
                                    iRegisterActivity.getToken(loginWechatBean.getDialog());
                                }
                            }else {
                                iRegisterActivity.onErro();

                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iRegisterActivity.onErro();
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
