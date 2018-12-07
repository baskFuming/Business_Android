package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.LanchScreenBean;
import com.zwonline.top28.model.HomeDetailsModel;
import com.zwonline.top28.view.ILanchScreenActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 启动页广告P层
 */
public class LanchScreenPresenter extends BasePresenter<ILanchScreenActivity> {
    public HomeDetailsModel homeDetailsModel;
    public ILanchScreenActivity iHomeDetails;

    public LanchScreenPresenter(ILanchScreenActivity iHomeDetailsActivity) {
        this.iHomeDetails = iHomeDetailsActivity;
        homeDetailsModel = new HomeDetailsModel();
    }

    /**
     * @param context 启动屏广告接口
     */
    public void lanchAD(Context context) {
        try {
            Flowable<LanchScreenBean> flowable = homeDetailsModel.lanchScreenAD(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<LanchScreenBean>() {
                        @Override
                        public void onNext(LanchScreenBean companyBean) {
                            if (companyBean.status==1){
                                iHomeDetails.successLanch(companyBean);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iHomeDetails.onErro();
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
