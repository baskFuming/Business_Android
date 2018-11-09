package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.BindWechatBean;
import com.zwonline.top28.model.RegisterModel;
import com.zwonline.top28.view.IbindWechatActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class BindWechatPresenter extends BasePresenter<IbindWechatActivity> {
    private IbindWechatActivity iRegisterActivity;
    private RegisterModel model;
    private Context context;
    public BindWechatPresenter(IbindWechatActivity iRegisterActivity, Context context) {
        this.iRegisterActivity = iRegisterActivity;
        model = new RegisterModel();
        this.context = context;
    }
    //绑定微信
    public void bindWechatNumber(final Context context, String union_id, String open_id, String gender, String nickname, String avatar, String country_code
            , String city, String province, String country, String language) {
        try {
            Flowable<BindWechatBean> flowable = model.bindWechat(context, union_id, open_id, gender, nickname, avatar, country_code, city, province, country, language);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<BindWechatBean>() {
                        @Override
                        public void onNext(BindWechatBean loginWechatBean) {
                            iRegisterActivity.bindWechat(loginWechatBean);
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
