package com.zwonline.top28.presenter;

import android.content.Context;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.BankBean;
import com.zwonline.top28.model.BankModel;
import com.zwonline.top28.view.IBankActivity;

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
public class BankPresenter extends BasePresenter<IBankActivity> {
    private BankModel bankModel;
    private IBankActivity iBankActivity;
    public BankPresenter(IBankActivity iBankActivity){
        this.iBankActivity=iBankActivity;
        bankModel=new BankModel();
    }
    //银行卡列表
    public void mBank(Context context){
        try {
            Flowable<BankBean>flowable=bankModel.BankList(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<BankBean>() {
                        @Override
                        public void onNext(BankBean bankBean) {
                            iBankActivity.showBank(bankBean.data);
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

    //银行卡解绑
    public void munBindBank(Context context,String id){
        try {
            bankModel.UnbindBank(context,id).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AmountPointsBean>() {
                        @Override
                        public void onNext(AmountPointsBean bankBean) {
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
