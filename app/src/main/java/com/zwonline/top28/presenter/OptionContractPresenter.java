package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.OptionContractBean;
import com.zwonline.top28.model.OptionContractModel;
import com.zwonline.top28.view.IOptionContractActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author YSG
 * @desc选择合同P层
 * @date ${Date}
 */
public class OptionContractPresenter extends BasePresenter<IOptionContractActivity> {
    private OptionContractModel optionContractModel;
    private IOptionContractActivity iOptionContractActivity;

    public OptionContractPresenter(IOptionContractActivity iOptionContractActivity) {
        this.iOptionContractActivity = iOptionContractActivity;
        optionContractModel = new OptionContractModel();
    }

    /**
     * 选择合同
     * @param context
     */
    public void mOptionContractModel(Context context) {
        try {
            Flowable<OptionContractBean> flowable = optionContractModel.optionContract(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<OptionContractBean>(context) {
                        @Override
                        protected void onBaseNext(OptionContractBean optionContractBean) {
                            Log.e("optionContractBean==", optionContractBean.msg);
                            if (optionContractBean.data.size()>0){
                                iOptionContractActivity.showOptionContract(optionContractBean.data);
                                iOptionContractActivity.noContract(true);
                            }else {
                                iOptionContractActivity.noContract(false);
                            }
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
     * 选择合同模板
     * @param context
     */
    public void mOptionContractModels(Context context) {
        try {
            Flowable<OptionContractBean> flowable = optionContractModel.optionContracts(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<OptionContractBean>(context) {
                        @Override
                        protected void onBaseNext(OptionContractBean optionContractBean) {
                            Log.e("optionContractBean==", optionContractBean.msg);
                                iOptionContractActivity.showOptionContract(optionContractBean.data);

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
