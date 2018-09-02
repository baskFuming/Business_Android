package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.BalanceRechargeBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.model.BalanceRechargeModel;
import com.zwonline.top28.model.PaymentModel;
import com.zwonline.top28.view.IBalanceRechargeActivity;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 余额充值
 * Created by sdh on 2018/3/14.
 */

public class BalanceRechargePresenter extends BasePresenter<IBalanceRechargeActivity> {
    private BalanceRechargeModel balanceRechargeModel;
    private PaymentModel paymentModel;
    private IBalanceRechargeActivity iBalanceRechargeActivity;

    public BalanceRechargePresenter(IBalanceRechargeActivity iBalanceRechargeActivity) {
        this.iBalanceRechargeActivity = iBalanceRechargeActivity;
        paymentModel = new PaymentModel();
        balanceRechargeModel = new BalanceRechargeModel();
    }


    /**
     * 获取支付宝orderStr
     * @param context
     * @param rchType
     */
    public void walletBalanceRecharge(Context context, Double amount, String rchType) {
        try {
            balanceRechargeModel.walletBalanceRecharge(context, amount, rchType).
                    subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<BalanceRechargeBean>(context) {

                        @Override
                        protected void onBaseNext(BalanceRechargeBean balanceRechargeBean) {
                            iBalanceRechargeActivity.walletBalanceRecharge(balanceRechargeBean);
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
     *
     *银行卡订单ID
     * @param context
     * @param rchType
     */
    public void walletBalanceRecharges(Context context, Double amount, String rchType) {
        try {
            balanceRechargeModel.walletBalanceRecharges(context, amount, rchType).
                    subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<AmountPointsBean>(context) {

                        @Override
                        protected void onBaseNext(AmountPointsBean amountPointsBean) {
                            iBalanceRechargeActivity.walletBalanceRecharges(amountPointsBean);
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
     * 根据ID获取支付详情
     *
     * @param context
     * @param orderId
     */
    public void getPayOrderInfoByOrder(Context context, String orderId) {
        try {
            balanceRechargeModel.getOrderInfos(context, orderId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<OrderInfoBean>() {
                        @Override
                        public void onNext(OrderInfoBean prepayPayBean) {
                            iBalanceRechargeActivity.showOrderInfo(prepayPayBean.data);
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
     * 返回积分单价接口
     * @param context
     */
    public void getUnitPrice(Context context){
        try {
            balanceRechargeModel.mUnitPrice(context).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AmountPointsBean>() {
                        @Override
                        public void onNext(AmountPointsBean amountPointsBean) {
                            iBalanceRechargeActivity.IUnitPriceId(amountPointsBean);
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

