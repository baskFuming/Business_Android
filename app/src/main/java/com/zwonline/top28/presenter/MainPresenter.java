package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.HongbaoPermissionBean;
import com.zwonline.top28.bean.RegisterRedPacketsBean;
import com.zwonline.top28.bean.UnclaimedMbpCountBean;
import com.zwonline.top28.bean.UpdateCodeBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.model.MainModel;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.IMainActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * MainActivity的P层
 */
public class MainPresenter extends BasePresenter<IMainActivity> {
    private MainModel mainModel;
    private IMainActivity iMainActivity;

    public MainPresenter(IMainActivity iMainActivity) {
        mainModel = new MainModel();
        this.iMainActivity = iMainActivity;
    }


    /**
     * 版本自动更新
     *
     * @param context
     * @param platform
     * @param version_code
     */
    public void UpdataVersion(Context context, String platform, String version_code) {
        try {
            Flowable<UpdateCodeBean> flowable = mainModel.updataVersion(context, platform, version_code);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<UpdateCodeBean>() {
                        @Override
                        public void onNext(UpdateCodeBean attentionBean) {
                            iMainActivity.showUpdataVersion(attentionBean);
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

    /**
     * 红包权限接口
     *
     * @param context
     */
    public void HongBaoPermission(final Context context) {
        try {
            Flowable<HongbaoPermissionBean> flowable = mainModel.hBaoPermission(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HongbaoPermissionBean>() {
                        @Override
                        public void onNext(HongbaoPermissionBean attentionBean) {
                            iMainActivity.showHongBaoPermission(attentionBean);
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

    /**
     * 鞅分未领取
     *
     * @param context
     */
    public void UnclaimedMbpCount(final Context context) {
        try {
            Flowable<UnclaimedMbpCountBean> flowable = mainModel.claimedMbpCount(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<UnclaimedMbpCountBean>() {
                        @Override
                        public void onNext(UnclaimedMbpCountBean attentionBean) {
                            if (attentionBean.status == 1) {
                                iMainActivity.showUnclaimedMbpCount(attentionBean);
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

    /**
     * 弹窗接口
     * dialogType==1是否要显示新人注册红包==2点击领取红包接口
     *
     * @param context
     */
    public void RegisterRedPacketDialogs(final Context context, String type, final String dialogType) {
        try {
            Flowable<RegisterRedPacketsBean> flowable = mainModel.mShowDialog(context, type);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<RegisterRedPacketsBean>(context) {
                        @Override
                        protected void onBaseNext(RegisterRedPacketsBean registerRedPacketsBean) {
                            if (registerRedPacketsBean.status == 1) {
                                if (StringUtil.isNotEmpty(dialogType) && dialogType.equals(BizConstant.NEW)) {
                                    //是否显示新人注册红包
                                    iMainActivity.showRedPacketDialog(registerRedPacketsBean.data.dialog_item.register_red_packet);
                                } else if (StringUtil.isNotEmpty(dialogType) && dialogType.equals(BizConstant.TYPE_TWO)) {
                                    //点击领取新人红包
                                    iMainActivity.showGetRedPacketDialog(registerRedPacketsBean.data.dialog_item.show_register_red_packet);
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
