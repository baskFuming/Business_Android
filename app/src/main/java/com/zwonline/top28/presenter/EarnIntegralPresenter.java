package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.EarnIntegralBean;
import com.zwonline.top28.model.EarnIntegralModel;
import com.zwonline.top28.view.IEarnIntegralActivity;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author YSG
 * @desc赚取积分的P层
 * @date ${Date}
 */
public class EarnIntegralPresenter extends BasePresenter<IEarnIntegralActivity> {
    public EarnIntegralModel earnIntegralModel;
    public IEarnIntegralActivity iIntegralActivity;


    /**
     * 赚取积分
     * @param iEarnIntegralActivity
     */
    public EarnIntegralPresenter(IEarnIntegralActivity iEarnIntegralActivity) {
        this.iIntegralActivity = iEarnIntegralActivity;
        earnIntegralModel = new EarnIntegralModel();
    }
    public void showEarnItegral(Context context){
        try {
            earnIntegralModel.showIntegralList(context).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<EarnIntegralBean>() {
                        @Override
                        public void onNext(EarnIntegralBean earnIntegralBean) {
                            iIntegralActivity.showEarnIngral(earnIntegralBean.data);
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
