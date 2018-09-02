package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AddClauseBean;
import com.zwonline.top28.bean.AddContractBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.SignContractBean;
import com.zwonline.top28.model.CustomContractModel;
import com.zwonline.top28.view.ICustomContractActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author YSG
 * @desc生成合同P层
 * @date ${Date}
 */
public class CustomContractPresenter extends BasePresenter<ICustomContractActivity> {
    private CustomContractModel customContractModel;
    private ICustomContractActivity iCustomContractActivity;

    public CustomContractPresenter(ICustomContractActivity iCustomContractActivity) {
        this.iCustomContractActivity = iCustomContractActivity;
        customContractModel = new CustomContractModel();
    }

    /**
     * 生成合同
     *
     * @param context
     */
    public void mCustomContractModel(Context context, String contract_id) {
        try {
            Flowable<AddClauseBean> flowable = customContractModel.customContract(context, contract_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<AddClauseBean>(context) {
                        @Override
                        protected void onBaseNext(AddClauseBean addClauseBean) {
                            iCustomContractActivity.showCustomContract(addClauseBean.data.terms);
                            iCustomContractActivity.showCustomContracts(addClauseBean);
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
     * 添加合同
     *
     * @param context
     */
    public void mAddContract(Context context, String project_id, String template_id, String title
            , String begin_date, String end_date, String terms) {
        try {
            Flowable<AddContractBean> flowable = customContractModel.addContract(context, project_id, template_id, title, begin_date, end_date, terms);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<AddContractBean>(context) {
                        @Override
                        protected void onBaseNext(AddContractBean settingBean) {
                            iCustomContractActivity.showAddContract(settingBean);
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
     * 扫码签署合同
     *
     * @param context
     */
    public void mSignContract(Context context, String orderId) {
        try {
            Flowable<SignContractBean> flowable = customContractModel.signContract(context, orderId);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<SignContractBean>(context) {
                        @Override
                        protected void onBaseNext(SignContractBean signContractBean) {
                            iCustomContractActivity.showSignContract(signContractBean.data.terms);
                            iCustomContractActivity.showSinContractTime(signContractBean);
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
