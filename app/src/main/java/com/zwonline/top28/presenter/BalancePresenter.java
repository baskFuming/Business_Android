package com.zwonline.top28.presenter;


import android.content.Context;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.WithdrawRecordBean;
import com.zwonline.top28.model.BalanceModel;
import com.zwonline.top28.view.IBalanceActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 描述：提现和提现记录
 *
 * @author YSG
 * @date 2018/1/10
 */
public class BalancePresenter extends BasePresenter<IBalanceActivity> {
    private BalanceModel balanceModel;
    private IBalanceActivity iBalanceActivity;

    public BalancePresenter(IBalanceActivity iBalanceActivity) {
        this.iBalanceActivity = iBalanceActivity;
        balanceModel = new BalanceModel();
    }

    //提现
    public void mWithdraw(Context context, int amount, String bank_card_id) {
        try {
            Flowable<HeadBean> flowable = balanceModel.Withdraw(context, amount, bank_card_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HeadBean>() {
                        @Override
                        public void onNext(HeadBean headBean) {

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

    //提现记录
    public void mWithdrawRecord(Context context, String page) {
        try {
            Flowable<WithdrawRecordBean> flowable = balanceModel.withdrawRecord(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<WithdrawRecordBean>() {
                        @Override
                        public void onNext(WithdrawRecordBean withdrawRecordBean) {
                            if (withdrawRecordBean.data.size()>0){
                                iBalanceActivity.showWithdrawRecordData(true);
                                iBalanceActivity.showWithdrawRecord(withdrawRecordBean.data);
                            }else {
                                iBalanceActivity.showWithdrawRecordData(false);
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
}
