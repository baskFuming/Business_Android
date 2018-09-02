package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.BalanceBean;
import com.zwonline.top28.bean.PaymentBean;
import com.zwonline.top28.model.WalletModel;
import com.zwonline.top28.view.IPayMentView;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class WallletPresenter extends BasePresenter<IPayMentView> {
    private WalletModel walletModel;
    private IPayMentView iPayMentView;
    public WallletPresenter(IPayMentView iPayMentView){
        this.iPayMentView=iPayMentView;
        walletModel=new WalletModel();
    }
    //我的收付款记录
    public void mPayMent(Context context,String page){
        try {
            Flowable<PaymentBean>flowable=walletModel.payMent(context,page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<PaymentBean>() {
                        @Override
                        public void onNext(PaymentBean paymentBean) {
                            if (paymentBean!=null&& paymentBean.data!=null&&paymentBean.data.size()>0){
                                if (paymentBean.data.size()>0){
                                    iPayMentView.showPayMent(true);
                                    iPayMentView.showPayMentData(paymentBean.data);
                                }else {
                                    iPayMentView.showPayMent(false);
                                }
                            }else {
                                iPayMentView.showOnErro();
                            }

                        }

                        @Override
                        public void onError(Throwable t) {
                            iPayMentView.showOnErro();
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
    public  void mBalances(Context context){
        try {
            Flowable<BalanceBean>flowable=walletModel.Balance(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<BalanceBean>() {
                        @Override
                        public void onNext(BalanceBean balanceBean) {
                            iPayMentView.showBalance(balanceBean);
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
    public  void mBalance(Context context){
        try {
            Flowable<BalanceBean>flowable=walletModel.Balance(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<BalanceBean>(context) {
                        @Override
                        protected void onBaseNext(BalanceBean balanceBean) {
                            iPayMentView.showBalance(balanceBean);
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
