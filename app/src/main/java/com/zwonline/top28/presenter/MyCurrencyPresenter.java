package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.IntegralBean;
import com.zwonline.top28.bean.MyCurrencyBean;
import com.zwonline.top28.model.MyCurrencyModel;
import com.zwonline.top28.view.IMyCurrencyActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;


/**
 * 描述：我的算力
 *
 * @author YSG
 * @date 2017/12/25
 */
public class MyCurrencyPresenter extends BasePresenter<IMyCurrencyActivity> {
    private MyCurrencyModel myCurrencyModel;
    private IMyCurrencyActivity iMyCurrencyActivity;

    public MyCurrencyPresenter(IMyCurrencyActivity iMyCurrencyActivity) {
        this.iMyCurrencyActivity = iMyCurrencyActivity;
        myCurrencyModel = new MyCurrencyModel();
    }

    //我的创业币
    public void mMyCurrency(Context context) {
        try {
            Flowable<MyCurrencyBean> flowable = myCurrencyModel.myCurrency(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyCurrencyBean>() {
                        @Override
                        public void onNext(MyCurrencyBean myCurrencyBean) {
                            Log.e("myCurrencyBean==", myCurrencyBean.msg);
                            iMyCurrencyActivity.showMyCurrencyData(myCurrencyBean.data.list);
                            iMyCurrencyActivity.showMyCurrency(myCurrencyBean);
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

    public void mMyCurrencys(Context context) {
        try {
            Flowable<MyCurrencyBean> flowable = myCurrencyModel.myCurrency(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyCurrencyBean>(context) {
                        @Override
                        protected void onBaseNext(MyCurrencyBean myCurrencyBean) {
                            Log.e("myCurrencyBean==", myCurrencyBean.msg);
                            iMyCurrencyActivity.showMyCurrencyData(myCurrencyBean.data.list);
                            iMyCurrencyActivity.showMyCurrency(myCurrencyBean);
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

    /**
     * 商机币
     *
     * @param context
     * @param type
     * @param page
     */
    public void BalanceLog(Context context, String type, int page) {
        try {
            Flowable<IntegralBean> flowable = myCurrencyModel.mBalanceLog(context, type, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<IntegralBean>(context) {
                        @Override
                        protected void onBaseNext(IntegralBean myCurrencyBean) {
                            Log.e("myCurrencyBean==", myCurrencyBean.msg);
                            iMyCurrencyActivity.showBalanceLog(myCurrencyBean);
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
