package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BalanceBean;
import com.zwonline.top28.bean.BalancePayBean;
import com.zwonline.top28.bean.IntegralPayBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.bean.PrepayPayBean;
import com.zwonline.top28.model.IntegralPayModel;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.IIntegralPayActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 积分充值p层
 * Created by sdh on 2018/3/13.
 */

public class IntegralPayPresenter extends BasePresenter<IIntegralPayActivity> {
    private IntegralPayModel interalPayModel;
    private IIntegralPayActivity iIntegralPayActivity;

    public IntegralPayPresenter(IIntegralPayActivity iIntegralPayActivity) {
        this.iIntegralPayActivity = iIntegralPayActivity;
        interalPayModel = new IntegralPayModel();
    }

    /**
     * 积分充值支付
     *
     * @param context
     */
    public void pointRecharge(Context context, final String rechType, String amount) {
        try {
            interalPayModel.getAmountByPoints(context, rechType, amount).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<IntegralPayBean>(context) {

                        @Override
                        protected void onBaseNext(IntegralPayBean integralPayBean) {
                            iIntegralPayActivity.pointsRecharge(integralPayBean);
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


//                        @Override
//                        public void onComplete() {
//                            if (!StringUtil.isEmpty(rechType)) {
//                                if (BizConstant.ALIPAY_METHOD.equals(rechType)) {
//                                    iIntegralPayActivity.initAlipayOrderStr();
//                                } else if (BizConstant.UNIONPAY_METHOD.equals(rechType)) {
//
//                                } else if (BizConstant.POS_METHOD.equals(rechType)) {
//                                    iIntegralPayActivity.initOrderInfo();
//                                }
//                                                            iIntegralPayActivity.showOrderInfo();
//                            }
//                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的积分充值金额
     *
     * @param context
     * @param points
     */
    public void getPointIntergralAmount(Context context, String points) {
        try {
            interalPayModel.pointsRecharge(context, points).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<AmountPointsBean>(context) {

                        @Override
                        protected void onBaseNext(AmountPointsBean amountPointsBean) {
                            iIntegralPayActivity.getPointsRechargeBackAmount(amountPointsBean);
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
     * 获取支付宝orderStr
     *
     * @param context
     * @param orderId
     */
    public void getPayOrderInfoByOrderId(Context context, String orderId) {
        try {
            interalPayModel.getOrderInfoById(context, orderId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<PrepayPayBean>() {
                        @Override
                        public void onNext(PrepayPayBean prepayPayBean) {
                            iIntegralPayActivity.getOrderInfoByOrderId(prepayPayBean);
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
     *
     * @param context
     */
    public void getUnitPrice(Context context) {
        try {
            interalPayModel.mUnitPrice(context).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<AmountPointsBean>(context) {


                        @Override
                        protected void onBaseNext(AmountPointsBean amountPointsBean) {
                            iIntegralPayActivity.IUnitPriceId(amountPointsBean);
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
            interalPayModel.getOrderInfo(context, orderId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<OrderInfoBean>() {

                        @Override
                        public void onNext(OrderInfoBean prepayPayBean) {
                            iIntegralPayActivity.showOrderInfo(prepayPayBean);
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

    //余额
    public void mBalances(Context context) {
        try {
            Flowable<BalanceBean> flowable = interalPayModel.Balance(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<BalanceBean>() {
                        @Override
                        public void onNext(BalanceBean balanceBean) {
                            iIntegralPayActivity.showBalance(balanceBean);
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

    //余额
    public void mBalancesPay(Context context, String amount) {
        try {
            Flowable<BalancePayBean> flowable = interalPayModel.getBalancePay(context, amount);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<BalancePayBean>(context) {

                        @Override
                        protected void onBaseNext(BalancePayBean balancePayBean) {
                            iIntegralPayActivity.showBalancePay(balancePayBean);
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
     * 充值商机币赠送算力
     *
     * @param context
     * @param businessOpportunityCoin
     * @param sortNum
     */
    public void GetPresentComputePower(final Context context, String businessOpportunityCoin, int sortNum) {
        try {
            Flowable<AttentionBean> flowable = interalPayModel.mGetPresentComputePower(context, businessOpportunityCoin, sortNum);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<AttentionBean>(context) {

                        @Override
                        protected void onBaseNext(AttentionBean balancePayBean) {
                            if (balancePayBean.status == 1) {
                                iIntegralPayActivity.showGetPresentComputePower(balancePayBean);
                            } else {
                                ToastUtils.showToast(context, balancePayBean.msg + "");
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
