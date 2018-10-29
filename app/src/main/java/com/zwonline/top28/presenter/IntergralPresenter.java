package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.IntegralBean;
import com.zwonline.top28.model.IntegralModel;
import com.zwonline.top28.view.IIntegralActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by sdh on 2018/3/10.
 * 积分P层
 */

public class IntergralPresenter extends BasePresenter<IIntegralActivity> {

    private IntegralModel integralModel;
    private IIntegralActivity iIntegralActivity;

    public IntergralPresenter(IIntegralActivity iIntegralActivity) {
        this.iIntegralActivity = iIntegralActivity;
        integralModel = new IntegralModel();
    }

    /**
     * 我的积分列表
     *
     * @param context
     */
    public void showIntergralList(Context context, String intergralType, String page) {
        try {
            integralModel.showIntegralList(context, intergralType, page).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<IntegralBean>() {
                        @Override
                        public void onNext(IntegralBean integralBean) {
                            iIntegralActivity.showIntegralListByTypeId(integralBean.data.list);
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
     * 全部积分列表
     *
     * @param context
     */
    public void showAllIntergralList(Context context, String page) {
        try {
            integralModel.showAllIntegralList(context, page).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<IntegralBean>() {
                        @Override
                        public void onNext(IntegralBean integralBean) {
                            iIntegralActivity.showIntegralListByTypeId(integralBean.data.list);
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
     * 商机币
     *
     * @param context
     * @param type
     * @param page
     */
    public void BalanceRecord(Context context, String type, int page) {
        try {
            Flowable<IntegralBean> flowable = integralModel.mBalanceRecord(context, type, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<IntegralBean>() {
                        @Override
                        public void onNext(IntegralBean integralBean) {
                            Log.e("myCurrencyBean==", integralBean.msg);
                            iIntegralActivity.showIntegralListByTypeId(integralBean.data.list);
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
