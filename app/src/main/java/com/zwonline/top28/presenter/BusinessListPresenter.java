package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.bean.BusinessListBean;
import com.zwonline.top28.model.BusinessModel;
import com.zwonline.top28.view.IBusinessListFrag;
import com.zwonline.top28.base.BasePresenter;
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
public class BusinessListPresenter extends BasePresenter<IBusinessListFrag> {
    private BusinessModel businessModel;
    private IBusinessListFrag iBusinessListFrag;

    public BusinessListPresenter(IBusinessListFrag iBusinessListFrag) {
        this.iBusinessListFrag = iBusinessListFrag;
        businessModel = new BusinessModel();
    }

    //    商机列表
    public void mBusinessList(Context context,String page, String cate_id) {
        try {
            Flowable<BusinessListBean> flowable = businessModel.business(context,page, cate_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<BusinessListBean>() {
                        @Override
                        public void onNext(BusinessListBean businessListBean) {
                            if (businessListBean != null && businessListBean.data != null && businessListBean.data.size() > 0) {
                                iBusinessListFrag.showData(businessListBean.data);
                            } else {
                                iBusinessListFrag.showErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
//                            iBusinessListFrag.showErro();
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
