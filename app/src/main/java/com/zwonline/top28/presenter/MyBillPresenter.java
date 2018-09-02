package com.zwonline.top28.presenter;

import android.content.Context;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.MyBillBean;
import com.zwonline.top28.model.MyBillModel;
import com.zwonline.top28.view.IMyBillActivity;

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
public class MyBillPresenter extends BasePresenter<IMyBillActivity> {
    private MyBillModel myBillModel;
    private IMyBillActivity iMyBillActivity;
    public MyBillPresenter(IMyBillActivity iMyBillActivity){
        this.iMyBillActivity=iMyBillActivity;
        myBillModel=new MyBillModel();
    }
    //我的账单
    public void mMyBill(Context context){
        try {
            Flowable<MyBillBean>flowable=myBillModel.myBill(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyBillBean>() {
                        @Override
                        public void onNext(MyBillBean myBillBean) {
                            if (myBillBean.data.size()>0){
                                iMyBillActivity.showMyBill(true);
                                iMyBillActivity.showMyBillData(myBillBean.data);
                            }else {
                                iMyBillActivity.showMyBill(false);
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
