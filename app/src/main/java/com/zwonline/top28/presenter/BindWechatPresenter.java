package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.BindWechatBean;
import com.zwonline.top28.bean.RegisterRedPacketsBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.model.BindWechatPhoneModel;
import com.zwonline.top28.model.RegisterModel;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.IbindWechatActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class BindWechatPresenter extends BasePresenter<IbindWechatActivity> {
    private IbindWechatActivity iRegisterActivity;
    private BindWechatPhoneModel model;
    private Context context;

    public BindWechatPresenter(IbindWechatActivity iRegisterActivity, Context context) {
        this.iRegisterActivity = iRegisterActivity;
        model = new BindWechatPhoneModel();
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


    /**
     * 弹窗接口
     * dialogType==1绑定微信弹窗==2绑定微信成功弹窗==3绑定手机号弹窗==4绑定手机号成功弹窗
     *
     * @param context
     */
    public void Dialogs(final Context context, String type, final String dialogType) {
        try {
            Flowable<RegisterRedPacketsBean> flowable = model.mShowDialog(context, type);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<RegisterRedPacketsBean>(context) {
                        @Override
                        protected void onBaseNext(RegisterRedPacketsBean registerRedPacketsBean) {
                            if (registerRedPacketsBean.status == 1) {
                                if (StringUtil.isNotEmpty(dialogType) && dialogType.equals(BizConstant.TYPE_ONE)) {
                                    //1绑定微信弹窗
                                    iRegisterActivity.showBindWechatPop(registerRedPacketsBean.data.dialog_item.wx_bind);
                                } else if (StringUtil.isNotEmpty(dialogType) && dialogType.equals(BizConstant.TYPE_TWO)) {
                                    //2绑定微信成功弹窗
                                    iRegisterActivity.showBindWechatSuccess(registerRedPacketsBean.data.dialog_item.wx_bind_success);
                                } else if (StringUtil.isNotEmpty(dialogType) && dialogType.equals(BizConstant.TYPE_THREE)) {
                                    //3绑定手机号弹窗
                                    iRegisterActivity.showBindMobile(registerRedPacketsBean.data.dialog_item.mobile_bind);
                                } else if (StringUtil.isNotEmpty(dialogType) && dialogType.equals(BizConstant.TYPE_FOUR)) {
                                    //4绑定手机号成功弹窗
                                    iRegisterActivity.showBindMobileSuccess(registerRedPacketsBean.data.dialog_item.mobile_bind_success);
                                }
                            } else {
                                ToastUtils.showToast(context, registerRedPacketsBean.msg);
                            }
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
}
