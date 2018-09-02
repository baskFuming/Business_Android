package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.model.AddBankModel;
import com.zwonline.top28.view.IAddBankActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;


/**
 * 描述：添加银行卡
 *
 * @author YSG
 * @date 2017/12/26
 */
public class AddBankPresenter extends BasePresenter<IAddBankActivity> {
    private AddBankModel addBankModel;
    private IAddBankActivity iAddBankActivity;

    public AddBankPresenter(IAddBankActivity iAddBankActivity) {
        this.iAddBankActivity = iAddBankActivity;
        addBankModel = new AddBankModel();
    }

    //添加银行卡
    public void mAddBank(Context context, String card_holder, String card_number, String card_bank) {
        try {

            Flowable<AddBankBean> flowable = addBankModel.addBank(context, card_holder, card_number, card_bank);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AddBankBean>() {
                        @Override
                        public void onNext(AddBankBean addBankBean) {
                            Log.i("xxx", addBankBean.msg);
                            iAddBankActivity.isSuccess(addBankBean);
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
