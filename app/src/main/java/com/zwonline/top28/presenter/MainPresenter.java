package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.HongbaoPermissionBean;
import com.zwonline.top28.bean.UnclaimedMbpCountBean;
import com.zwonline.top28.bean.UpdateCodeBean;
import com.zwonline.top28.model.MainModel;
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
    public void UnclaimedMbpCount(Context context) {
        try {
            Flowable<UnclaimedMbpCountBean> flowable = mainModel.claimedMbpCount(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<UnclaimedMbpCountBean>() {
                        @Override
                        public void onNext(UnclaimedMbpCountBean attentionBean) {
                            iMainActivity.showUnclaimedMbpCount(attentionBean);
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
